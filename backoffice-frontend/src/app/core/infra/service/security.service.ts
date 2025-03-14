import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { JwtHelperService } from '@auth0/angular-jwt';
import { SecurityConstants } from '../../model/security-constants.model';
import { User } from '../../model/user.model';
import { ToastrCustomService } from '../../toastr/toastr.service';
import { HttpConnectionBuilder } from '../http/http-connection.builder';
import { ExpirationDateService } from './expiration-date.service';
import { LocalStorageService } from './local-storage.service';


@Injectable()
export class SecurityService {

    private jwtHelper: JwtHelperService = new JwtHelperService();

    constructor(private http: HttpClient,
                private router: Router,
                private localStorageService: LocalStorageService,
                private expirationDateService: ExpirationDateService,
                private toastrService: ToastrCustomService) { }

    public login(user: any) {
        return new HttpConnectionBuilder<any>(this.http)
            .addEndPoint('login')
            .addHandlerSucess((response: HttpResponse<any>) => {
                this.router.navigate(['home']);
            })
            .addHandlerError(error => {
                this.toastrService.showWarningMessage('Login ou Senha Incorretas');
            })
            .addParameter(user)
            .buildPost();
    }

    public refreshToken(response: HttpResponse<any>): void {
        const header = response.headers.get(SecurityConstants.AUTH_HEADER);

        if (header) {
          const token = header.replace('Bearer ', '');
          this.localStorageService.push(SecurityConstants.TOKEN_KEY, token);
          this.localStorageService.push(SecurityConstants.USER, this.decodeToken());
          this.updateExpirationDate();
        }
    }

    public updateExpirationDate(): void {
        this.expirationDateService.updateDate(this.getExpirationDate());
    }

    public getExpirationDate(): Date {
        const token = this.tokenKey;
        return this.jwtHelper.getTokenExpirationDate(token);
    }

    public logoutExpiratedSession(): void {
        this.toastrService.showInfoMessage('Sua Sessão Expirou!');
        this.logout();
    }

    public logout(): void {
        this.expirationDateService.updateDate(null);
        this.localStorageService.clearAll();
        this.router.navigate(['']);
    }

    public hasAuthority(authority: string): boolean {
        if (authority) {
            const user = this.decodeToken();
            const authorities = user.authorities.filter(a => a === authority).map(u => u.authorities);
            return authorities.length > 0;
        }

        return false;
    }


    public hasAnyAuthority(authorities: string[]): boolean {
        if (authorities) {
            const user = this.decodeToken();
            for (const authority of authorities) {
                const auths = user.authorities.filter(a => a === authority).map(u => u.authorities);
                if (auths.length > 0) {
                    return true;
                }
            }
        }

        return false;
    }


    public decodeToken(): User {
        const userAuth = this.jwtHelper.decodeToken(this.tokenKey);
        const user = userAuth;
        user.username = userAuth.sub;
        return user;
    }

    public isTokenExpired(): boolean {
        if (this.tokenKey) {
            return this.jwtHelper.isTokenExpired(this.tokenKey);
        }
        return true;
    }

    public clearAll(): void {
        this.localStorageService.clearAll();
    }

    get tokenKey(): string {
        return this.localStorageService.pull(SecurityConstants.TOKEN_KEY);
    }

    get token(): string {
        return this.jwtHelper.decodeToken(this.tokenKey);
    }
}

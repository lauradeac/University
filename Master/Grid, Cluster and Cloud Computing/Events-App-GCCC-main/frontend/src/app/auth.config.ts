import { AuthConfig } from 'angular-oauth2-oidc';

export const authConfig: AuthConfig = {
  issuer: 'https://login.microsoftonline.com/5a4863ed-40c8-4fd5-8298-fbfdb7f13095/v2.0',
  redirectUri: window.location.origin,
  clientId: 'd7ef7e1c-ea15-4960-89d6-7ef0f0f2a7b8',
  responseType: 'code',
  strictDiscoveryDocumentValidation: false,
  scope: 'openid api://d7ef7e1c-ea15-4960-89d6-7ef0f0f2a7b8/api.scope',
}

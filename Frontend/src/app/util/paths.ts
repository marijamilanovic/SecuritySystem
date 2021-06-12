export const BACKEND_PATH ='https://localhost:5051';

export const CERTIFICATE_PATH = BACKEND_PATH + '/certificate';
export const CERTIFICATE_ISSUERS_PATH = CERTIFICATE_PATH + '/issuers';
export const CERTIFICATE_CHAIN_PATH = CERTIFICATE_PATH + '/chain';
export const ROOT_PATH = CERTIFICATE_PATH + '/root';
export const REVOKE_PATH = CERTIFICATE_PATH + '/revoke';
export const CERTIFICATE_TYPES_PATH = CERTIFICATE_PATH + '/types';

export const CERTIFICATE_VALIDATION_PATH = BACKEND_PATH + '/validation';

export const PDF_PATH = BACKEND_PATH + '/download';

export const USER_PATH = BACKEND_PATH + '/user';
export const LOGIN_PATH = USER_PATH + '/login';
export const REGISTER_PATH = USER_PATH + '/register';

export const CERT_REQUEST_PATH = BACKEND_PATH + '/request';
export const USERS_REQUESTS = CERT_REQUEST_PATH + '/user';

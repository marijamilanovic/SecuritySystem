export const BACKEND_PATH ='https://localhost:5051';

export const CERTIFICATE_PATH = BACKEND_PATH + '/certificate';
export const CERTIFICATE_TYPES_PATH = CERTIFICATE_PATH + '/types';
export const CERTIFICATE_ISSUERS_PATH = CERTIFICATE_PATH + '/issuers';
export const CERTIFICATE_CHAIN_PATH = CERTIFICATE_PATH + '/chain';

export const CERTIFICATE_VALIDATION_PATH = BACKEND_PATH + '/validation';

export const REVOKE_PATH = CERTIFICATE_PATH + '/revoke';

export const ROOT_PATH = CERTIFICATE_PATH + '/root';

export const PDF_PATH = BACKEND_PATH + '/download';
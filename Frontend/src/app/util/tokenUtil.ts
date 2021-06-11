import jwt_decode from 'jwt-decode';

export function getToken() : any {
    return localStorage.getItem("JWT");
}

export function saveToken(token : string) {    
    localStorage.setItem("JWT", token);
}

export function getParsedToken() : any{
    try{
        return jwt_decode(getToken());
    }
    catch(Error){
        return null;
    }
}

export function removeToken() {
    localStorage.removeItem("JWT");
}
export function getUsernameFromToken() {
    let parsedToken=getParsedToken();
    if(parsedToken == null){
        return null;
    }
    return parsedToken.sub;
}

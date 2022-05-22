package com.becomedigital.sdk.identity.becomedigitalsdk.utils;

public interface SharedParameters {
     enum typeDocument {
        DNI,
        PASSPORT,
        LICENSE
    }
    String URL_ADD_DATA = "URL_ADD_DATA";
    String URL_ADD_DOCUMENT_DATA = "URL_ADD_DOCUMENT_DATA";
    String URL_RE_VALIDATION = "URL_RE_VALIDATION";
    String URL_GET_CONTRACT= "URL_RE_VALIDATION";
    String URL_AUTH = "URL_ACTIVATION_DATA_SERVER";
    String client_id_mati = "5edaa0346464c3001b5c5832";
    String secretKeyMati = "P2F0WFX7CY87JJVEQDEKZXMPEHGHJA1G";
    String url_auth = "https://becomedigital-api.azurewebsites.net/api/v1/auth";
    String url_add_data = "https://becomedigital-api.azurewebsites.net/api/v1/identity";
    String url_add_document_data = "https://becomedigital-api.azurewebsites.net/api/v1/idrnd_doc";
    String url_re_validation = "https://becomedigital-api.azurewebsites.net/api/v1/matches";
    String url_get_contract = "https://becomedigital-api.azurewebsites.net/api/v1/contract/";
}

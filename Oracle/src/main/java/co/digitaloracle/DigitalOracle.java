/**
 * 
 */
package co.digitaloracle;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import co.digitaloracle.api.ApiListener;
import co.digitaloracle.api.ApiManager;
import co.digitaloracle.api.ApiResponse;
import co.digitaloracle.model.KeychainParams;
import co.digitaloracle.model.SignatureRequest;

import com.github.shaxbee.uuid.UUID;
import com.github.shaxbee.uuid.UUID.Namespace;

import org.codehaus.jackson.JsonProcessingException;

/**
 * Copyright (C) 2014 CryptoCorp. All rights reserved.
 * 
 * @author liorsaar
 * 
 */
public class DigitalOracle extends ApiManager {

    private String mHostUrl;

    public DigitalOracle(String aHostUrl) {
        if (aHostUrl == null)
            throw new NullPointerException("@param:aHostUrl");
        mHostUrl = aHostUrl.trim();
    }

    /**
     * @param aKeychainId
     * @param aListener
     */
    public void getKeychain(String aKeychainId, ApiListener aListener) {
        if (aKeychainId == null)
            throw new NullPointerException("@param:aKeychainId");
        String keychainUrl = getKeychainUrl(aKeychainId);
        get(keychainUrl, aListener);
    }

    /**
     * @param aKeychainParams
     * @param aListener
     * @throws JsonProcessingException
     * @throws Exception
     */
    public void createKeychain(KeychainParams aKeychainParams, ApiListener aListener) throws IOException {
        String key = aKeychainParams.getKeys().get(0);
        String keychainId = getNewKeychainId(key);
        String keychainUrl = getKeychainUrl(keychainId);
        post(keychainUrl, ApiResponse.toJsonString(aKeychainParams), aListener);
    };

    /**
     * @param aKeychainId
     * @param aSignatureRequest
     * @param aListener
     * @throws UnsupportedEncodingException
     * @throws JsonProcessingException
     */
    public void signTx(String aKeychainId, SignatureRequest aSignatureRequest, ApiListener aListener) throws IOException {
        String keychainUrl = getKeychainTxUrl(aKeychainId);
        post(keychainUrl, ApiResponse.toJsonString(aSignatureRequest), aListener);
    }

    /**
     * @param aKey
     * @return UUID v5 from urn+key
     * @see <a href="https://cryptocorp.co/api/"/>
     */
    private String getNewKeychainId(String aKey) {
        String name = "urn:digitaloracle.co:" + aKey;
        UUID uuid = UUID.uuid5(Namespace.DNS, name.getBytes());
        return uuid.hex();
    }

    private String getKeychainUrl(String aKeychinId) {
        return mHostUrl + "/keychains/" + aKeychinId;
    }

    private String getKeychainTxUrl(String aKeychinId) {
        return getKeychainUrl(aKeychinId) + "/transactions";
    }

    public static void main(String [ ] args) {
        return;
    }

}
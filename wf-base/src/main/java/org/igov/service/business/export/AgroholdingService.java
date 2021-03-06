/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.igov.service.business.export;

import com.google.common.io.Files;
import org.igov.io.GeneralConfig;
import org.igov.io.web.HttpRequester;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.File;
import java.nio.charset.Charset;
import org.igov.io.fs.FileSystemData;

/**
 *
 * @author olga
 */
@Service
public class AgroholdingService {

    private static final Logger LOG = LoggerFactory.getLogger(AgroholdingService.class);

    @Autowired
    HttpRequester httpRequester;

    @Autowired
    GeneralConfig generalConfig;

    public String transferDocumentVacation(String documentVacation) throws Exception {
        httpRequester.setsLogin(generalConfig.getsLogin_Auth_Agroholding());
        httpRequester.setsPassword(generalConfig.getsPassword_Auth_Agroholding());
        String sURL = generalConfig.getsURL_Agroholding() + "/Document_ОтпускаОрганизаций";
        LOG.info("sURL: " + sURL);
        //http://spirit.mriya.ua:2011/trainingbase/odata/standard.odata/Document_ОтпускаОрганизаций
        String result = "none";
        result = httpRequester.postInside(sURL, null, documentVacation, "application/atom+xml;type=feed;charset=utf-8");
        LOG.info("nResponseCode: " + httpRequester.getnResponseCode() + " result: " + result);
        return result;
    }
}

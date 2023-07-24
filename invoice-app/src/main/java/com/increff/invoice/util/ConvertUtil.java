package com.increff.invoice.util;


import com.increff.invoice.model.form.InvoiceForm;
import org.apache.fop.apps.*;
import org.springframework.stereotype.Component;


import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.util.Base64;

@Component
public class ConvertUtil {

    public void pojo2XmlConverter(InvoiceForm form) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(InvoiceForm.class);
        Marshaller marshaller = jaxbContext.createMarshaller();
        File file = new File("src/main/resources/invoice.xml");
        marshaller.marshal(form, file);
    }

    public String xml2base64(){
        try {
            File xsltFile = new File("src/main/resources/invoice-template.xsl");
            StreamSource xsltSource = new StreamSource(xsltFile);

            File xmlFile = new File("src/main/resources/invoice.xml");
            StreamSource xmlSource = new StreamSource(xmlFile);

            FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());


            ByteArrayOutputStream outStream = new ByteArrayOutputStream();

            FOUserAgent userAgent = fopFactory.newFOUserAgent();
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, userAgent, outStream);

            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(xsltSource);
            Result result = new SAXResult(fop.getDefaultHandler());
            transformer.transform(xmlSource, result);

            byte[] pdfBytes = outStream.toByteArray();

            return Base64.getEncoder().encodeToString(pdfBytes);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}





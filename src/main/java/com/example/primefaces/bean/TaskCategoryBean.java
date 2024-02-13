package com.example.primefaces.bean;

import java.io.IOException;

import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.parts.WordprocessingML.MainDocumentPart;
import org.docx4j.wml.Text;
import services.TaskCategoryService;
import lombok.Getter;
import lombok.Setter;
import models.TaskCategory;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ManagedBean
@SessionScoped
public class TaskCategoryBean {

    private List<TaskCategory> taskCategoryList = new ArrayList<>();
    @Inject
    private TaskCategoryService service = new TaskCategoryService();

    @PostConstruct
    public void init() {
        taskCategoryList = service.getAllTaskCategories();
    }

    // readHere --> https://www.baeldung.com/docx4j
    public void exportFromDocx4j() {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            externalContext.responseReset();
            externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"welcome.docx\"");

            OutputStream out = externalContext.getResponseOutputStream();
            WordprocessingMLPackage wordPackage = WordprocessingMLPackage.createPackage();
            MainDocumentPart mainDocumentPart = wordPackage.getMainDocumentPart();
            mainDocumentPart.addStyledParagraphOfText("Title", "Hello World!");
            mainDocumentPart.addParagraphOfText("Welcome To Baeldung");
            wordPackage.save(out);
        } catch (Docx4JException | IOException e) {
            e.printStackTrace();
        } finally {
            closeResources(facesContext);
        }
    }

    public void exportFromExistingDocx() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();

        try {
            externalContext.responseReset();
            externalContext.setResponseContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
            externalContext.setResponseHeader("Content-Disposition", "attachment; filename=\"ok.docx\"");

            OutputStream out = externalContext.getResponseOutputStream();

            try (InputStream inputStream = getClass().getResourceAsStream("/ok.docx")) {
                WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.load(inputStream);

                MainDocumentPart mainDocumentPart = wordMLPackage.getMainDocumentPart();
                String textNodesXPath = "//w:t";
                List<Object> textNodes = null;
                try {
                    textNodes = mainDocumentPart.getJAXBNodesViaXPath(textNodesXPath, true);
                } catch (JAXBException e) {
                    throw new RuntimeException(e);
                }

                for (Object obj : textNodes) {
                    if (((Text) ((JAXBElement<?>) obj).getValue()).getValue().equals("Ok")) {
                        ((Text) ((JAXBElement<?>) obj).getValue()).setValue("I_HAVE_CHANGED!");
                    }
                    Text text = (Text) ((JAXBElement) obj).getValue();
                    String textValue = text.getValue();
                    System.out.println(textValue);
                }

                wordMLPackage.save(out);
            } catch (Docx4JException | IOException e) {
                e.printStackTrace();
            }

            facesContext.responseComplete();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void closeResources(FacesContext facesContext) {
        try {
            facesContext.responseComplete();
            facesContext.getExternalContext().getResponseOutputStream().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

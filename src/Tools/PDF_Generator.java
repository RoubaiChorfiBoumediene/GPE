package Tools.PDF;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.languages.ArabicLigaturizer;
import com.itextpdf.text.pdf.languages.LanguageProcessor;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class PDF_Generator {

    public static LanguageProcessor arabicLP = new ArabicLigaturizer(); //arabic language processor

    public static final Font FONT0 = //Bigheader Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 16, Font.BOLD +Font.UNDERLINE);
    public static final Font FONT1 =//Header Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 13, Font.BOLD); //arabic font
    public static final Font FONT2 =//Title Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 32, Font.BOLD);
    public static final Font FONT3 =//Body Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 16, Font.NORMAL);
    public static final Font FONT4 =//Directer Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 17, Font.BOLD + Font.UNDERLINE);
    public static final Font FONT3b =//Body Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 18, Font.NORMAL);
    public static final Font FONT4b =//Directer Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 19, Font.BOLD + Font.UNDERLINE);
    public static final Font FONT5 =//Notice Font
            FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 13, Font.BOLD);


    private void createVacationPDF(String destination,String name,String grade,String vacationType,String
            duration,String from,String to) throws DocumentException, IOException {
        //getting the current date with specified format
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int monthInt = localDate.getMonthValue();
        String month = String.valueOf(monthInt);
        if(monthInt<10) month="0"+month;
        int dayInt   = localDate.getDayOfMonth();
        String day = String.valueOf(dayInt);
        if(dayInt<10) day="0"+day;

        //initialize the document
        Document doc = new Document();
        //setting the output stream of the document
        PdfWriter.getInstance(doc,new FileOutputStream(destination));
        //variable to process arabic text
        LanguageProcessor ar = new ArabicLigaturizer();
        //opening the document
        doc.open();
        //inserting data via tables
        PdfPTable tab2 = new PdfPTable(1);
        tab2.setWidthPercentage(100);
        //adding cell with text to the table
        tab2.addCell(getCellBigHeader("الجمهوريـة الجزائريــة الديمقراطيـة الشعبيـة",PdfPCell.ALIGN_CENTER));
        //adding the table to the document
        doc.add(tab2);
        //go to the next line
        doc.add(new Paragraph(" \n"));
        //delete the last row of the table (initializing the table)
        tab2.deleteLastRow();
        tab2.addCell(getCellBigHeader("وزارة التعليم العالي والبحث العلمي",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        PdfPTable tab = new PdfPTable(2);
        tab.setWidthPercentage(100);
        tab.addCell(getCellHeader("  ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("المدرسة العليا للإعلام الآلي -08 ماي 1945- سيدي بلعباس",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("المديرية - الأمانة العامة",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        tab.deleteLastRow();
        Image img = Image.getInstance("./src/Tools/Images/EsiSbaLogo.jpg");
        img.scaleAbsolute(60f,60f);
        img.setAbsolutePosition(30,675);
        doc.add(img);
        tab.deleteLastRow();
        doc.add(new Paragraph(" \n"));
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("مديرية المستخدمين و التكوين و النشاطات الثقافية و الرياضية",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("مصلحة المستخدمين المدرسين",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("الرقم: ........./"+year,PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph("\n \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellHeader("سـنـد عـطـلـة",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.add(new Paragraph("\n \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("الإسم و اللقب : "+name,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("الرتبة: "+grade,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("نوعية العطلة : "+vacationType,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("المدة : "+duration,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("من : "+from +"     "+"إلى : "+to,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph("\n \n"));
        tab2.deleteLastRow();
        tab2.addCell
                (getCellBodyVac("سيدي بلعباس في : "+ day+"/"+month+"/"+year,PdfPCell.ALIGN_LEFT));
        doc.add(tab2);
        doc.add(new Paragraph("\n \n \n \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBodyVac("المــــــديــــــر",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.close();
    }

    //the method which create pdf
    public void createAttestation(String destination, String name, String birthday,
                                  String birthplace, String grade, String dateOfRecrutement, Boolean gender, Boolean national) throws IOException, DocumentException {
        if(national) createAttestationArabe(destination,name,birthday, birthplace,grade,dateOfRecrutement,gender);
        else createAttestationFR(destination,name,birthday, birthplace,grade,dateOfRecrutement,gender);
    }

    private void createAttestationArabe(String destination,String name,String birthday,
                                        String birthplace,String grade,String dateOfRecrutement,Boolean gender) throws DocumentException, IOException {

        //getting the current date with specified format
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int year  = localDate.getYear();
        int monthInt = localDate.getMonthValue();
        String month = String.valueOf(monthInt);
        if(monthInt<10) month="0"+month;
        int dayInt   = localDate.getDayOfMonth();
        String day = String.valueOf(dayInt);
        if(dayInt<10) day="0"+day;

        //initialize the document
        Document doc = new Document();
        //setting the output stream of the document
        PdfWriter.getInstance(doc,new FileOutputStream(destination));
        //variable to process arabic text
        LanguageProcessor ar = new ArabicLigaturizer();
        //opening the document
        doc.open();
        //inserting data via tables
        PdfPTable tab2 = new PdfPTable(1);
        tab2.setWidthPercentage(100);
        //adding cell with text to the table
        tab2.addCell(getCellBigHeader("الجمهوريـة الجزائريــة الديمقراطيـة الشعبيـة",PdfPCell.ALIGN_CENTER));
        //adding the table to the document
        doc.add(tab2);
        //go to the next line
        doc.add(new Paragraph(" \n"));
        //delete the last row of the table (initializing the table)
        tab2.deleteLastRow();
        tab2.addCell(getCellBigHeader("وزارة التعليم العالي والبحث العلمي",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        PdfPTable tab = new PdfPTable(2);
        tab.setWidthPercentage(100);
        tab.addCell(getCellHeader("  ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("المدرسة العليا للإعلام الآلي -08 ماي 1945- سيدي بلعباس",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("المديرية - الأمانة العامة",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        tab.deleteLastRow();
        Image img = Image.getInstance("./src/Tools/Images/EsiSbaLogo.jpg");
        img.scaleAbsolute(60f,60f);
        img.setAbsolutePosition(30,675);
        doc.add(img);
        tab.deleteLastRow();
        doc.add(new Paragraph(" \n"));
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("مديرية المستخدمين و التكوين و النشاطات الثقافية و الرياضية",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("مصلحة المستخدمين المدرسين",PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph(" \n"));
        tab.deleteLastRow();
        tab.addCell(getCellHeader("   ",PdfPCell.ALIGN_LEFT));
        tab.addCell(getCellHeader("الرقم: ........./"+year,PdfPCell.ALIGN_RIGHT));
        doc.add(tab);
        doc.add(new Paragraph("\n \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellHeader("شـهـادة عـمـل",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.add(new Paragraph("\n  \n"));
        tab2.deleteLastRow();
        tab2.addCell
                (getCellBody("إن السـيـد مـديـر المـدرسـة العـليـا للإعلام الآلـي 08 ماي 1945 بسـيـدي بلعـبـاس يـشـهـد أن:",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        tab2.deleteLastRow();
        doc.add(new Paragraph(" \n"));
        if(gender)tab2.addCell(getCellBody("السيد: "+ name,PdfPCell.ALIGN_RIGHT));
        else tab2.addCell(getCellBody("السيدة: "+ name,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        if(gender)tab2.addCell(getCellBody("المولود في: "+ birthday+"   بـ"+birthplace,PdfPCell.ALIGN_RIGHT));
        else tab2.addCell(getCellBody("المولودة في: "+ birthday+"   بـ"+birthplace,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBody("الرتبة: "+grade,PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        if(gender)tab2.addCell(getCellBody("يعمل بالمدرسة العليا للإعلام الآلي 08 ماي 1945 بسيدي بالعباس ",PdfPCell.ALIGN_RIGHT));
        else tab2.addCell(getCellBody("تعمل بالمدرسة العليا للإعلام الآلي 08 ماي 1945 بسيدي بالعباس ",PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBody("إبتداء من: "+dateOfRecrutement+" إلى غاية يومنا هذا.",PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell
                (getCellBody("سلمت هذه الشهادة بطلب من المعني بالأمر للإستظهار بها في حدود ما يسمح به القانون.",PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n \n \n"));
        tab2.deleteLastRow();
        tab2.addCell
                (getCellBody("حرر بسيدي بلعباس يوم: "+ day+"/"+month+"/"+year,PdfPCell.ALIGN_LEFT));
        doc.add(tab2);
        doc.add(new Paragraph(" \n"));
        tab2.deleteLastRow();
        tab2.addCell(getCellBody("المــــديــــر",PdfPCell.ALIGN_CENTER));
        doc.add(tab2);
        doc.add(new Paragraph("\n \n \n \n \n"));
        Paragraph p = new Paragraph();
        p.add("______________________________________________________________________________");
        p.setAlignment(2);
        doc.add(p);
        tab2.deleteLastRow();
        tab2.addCell(getCellNotice("تنبيه: لا يمنح سوى نسختين من هذه الشهادة خلال السنة. لذا يتوجب على المعني إيداع النسخ في المعاملات الإدارية.",PdfPCell.ALIGN_RIGHT));
        doc.add(tab2);
        //closing the document
        doc.close();

    }


    private void createAttestationFR(String destination,String name,String birthday,
                                     String birthplace,String grade,String dateOfRecrutement,Boolean gender) throws IOException, DocumentException {
        //getting the current date with specified format
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int yearInt  = localDate.getYear();
        String year = String .valueOf(yearInt);
        int monthInt = localDate.getMonthValue();
        String month = String.valueOf(monthInt);
        if(monthInt<10) month="0"+month;
        int dayInt   = localDate.getDayOfMonth();
        String day = String.valueOf(dayInt);
        if(dayInt<10) day="0"+day;

        //initialize the document
        Document doc = new Document();
        doc.setMargins(36,36,10,10);
        //setting the output stream of the document
        PdfWriter.getInstance(doc,new FileOutputStream(destination));
        //variable to process arabic text
        LanguageProcessor ar = new ArabicLigaturizer();
        //opening the document
        doc.open();
        Paragraph paragraph = new Paragraph();
        paragraph.setFont(FONT0);
        paragraph.setAlignment(1);
        paragraph.setLeading(0,2);
        paragraph.add("République Algérienne Démocratique Et Populaire\n" +
                "Ministère de l’Enseignement Supérieur Et de la Recherche Scientifique\n");
        doc.add(paragraph);
        Image img = Image.getInstance("./src/Tools/Images/EsiSbaLogo.jpg");
        img.scaleAbsolute(60f,60f);
        img.setAbsolutePosition(500,665);
        doc.add(img);
        paragraph.clear();
        Font FONT11 = FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 14, Font.BOLD);
        paragraph.setFont(FONT11);
        paragraph.setAlignment(0);
        paragraph.add("\nEcole Supérieure en Informatique – 08 Mai 1945 " +
                "\nSidi bel Abbes\nDirection de l’école\nSecrétariat général\nN° :………/2017");
        doc.add(paragraph);
        paragraph.clear();
        Font FONT6 = FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 30, Font.BOLD+Font.UNDERLINE);
        paragraph.setFont(FONT6);
        paragraph.setAlignment(1);
        paragraph.add("Attestation de travail");
        doc.add(paragraph);
        paragraph.clear();
        Font FONT7 = FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 20, Font.BOLD);
        paragraph.setAlignment(1);
        paragraph.setFont(FONT7);
        paragraph.add("(Valable uniquement pour l’étranger)");
        doc.add(paragraph);
        paragraph.clear();
        paragraph.setAlignment(0);
        paragraph.setFont(FONT3);
        paragraph.add("   Le Directeur de l’école supérieure en informatique -08 mai 1945- de Sidi Bel Abbés, atteste que : \n");
        if(gender) paragraph.add("Mr : ");
        else paragraph.add("Mlle : ");
        Phrase input = new Phrase();
        Font FONT8 =//Input Font
                FontFactory.getFont("./src/Tools/Fonts/Arial/arial.ttf", BaseFont.IDENTITY_H, true, 16, Font.BOLD);
        input.setFont(FONT8);
        input.add(name);
        paragraph.add(input);
        doc.add(paragraph);
        paragraph.clear();
        if(gender) paragraph.add("Né le : ");
        else paragraph.add("Née le : ");
        input.clear();
        input.add(birthday);
        paragraph.add(input);
        paragraph.add(" à ");
        input.clear();
        input.add(birthplace);
        paragraph.add(input);
        doc.add(paragraph);
        paragraph.clear();
        paragraph.add("Nationalité : ");
        input.clear();
        input.add("Algérienne");
        paragraph.add(input);
        doc.add(paragraph);
        paragraph.clear();
        paragraph.add("Grade : ");
        input.clear();
        input.add(grade);
        paragraph.add(input);
        doc.add(paragraph);
        paragraph.clear();
        if(gender) paragraph.add("Est employé au sein de l’école supérieure en informatique -08 mai 1945- de Sidi Bel Abbés, depuis le ");
        else  paragraph.add("Exerce ses fonctions au sein de l’école supérieure en informatique de Sidi Bel Abbès, depuis le ");
        input.clear();
        input.add(dateOfRecrutement);
        paragraph.add(input);
        paragraph.add(" à ce jour.");
        doc.add(paragraph);
        paragraph.clear();
        paragraph.add("\n   Cette attestation lui est délivrée pour servir et valoir ce que de droit.");
        doc.add(paragraph);
        paragraph.clear();
        paragraph.setAlignment(2);
        paragraph.add("Fait à Sidi Bel Abbés le : ");
        input.clear();
        input.add(day+"/"+month+"/"+year);
        paragraph.add(input);
        doc.add(paragraph);
        paragraph.clear();
        input.clear();
        input.setFont(FONT4);
        input.add("Le Directeur");
        paragraph.add(input);
        paragraph.add("                                 ");
        doc.add(paragraph);
        doc.close();
    }
    //to add text(Big header) into cell after setting it's FONT and properties
    private PdfPCell getCellBigHeader(String text, int alignment) {
        Phrase phrase = new Phrase();
        phrase.setFont(FONT0);
        phrase.add(arabicLP.process(text));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    //to add text(headers) into cell after setting it's FONT and properties
    private PdfPCell getCellHeader(String text, int alignment) {
        Phrase phrase = new Phrase();
        if(alignment!=1) {phrase.setFont(FONT1); }
        else phrase.setFont(FONT2);
        phrase.add(arabicLP.process(text));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    //to add text(body) into cell after setting it's FONT and properties
    private PdfPCell getCellBody(String text, int alignment) {
        Phrase phrase = new Phrase();
        if(!text.equals("المــــديــــر")) phrase.setFont(FONT3);
        else phrase.setFont(FONT4);
        phrase.add(arabicLP.process(text));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    private PdfPCell getCellBodyVac(String text, int alignment) {
        Phrase phrase = new Phrase();
        if(!text.equals("المــــــديــــــر")) phrase.setFont(FONT3b);
        else phrase.setFont(FONT4b);
        phrase.add(arabicLP.process(text));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    //to add text(notice) into cell after setting it's FONT and properties
    private PdfPCell getCellNotice(String text, int alignment) {
        Phrase phrase = new Phrase();
        phrase.setFont(FONT5);
        phrase.add(arabicLP.process(text));
        PdfPCell cell = new PdfPCell(phrase);
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

}

package com.altran.iot.gui;

import com.altran.iot.QueryOperations;
import com.altran.iot.WriteOperations;
import com.altran.iot.observation.Observation;
import com.altran.iot.observation.ObservationsService;
import com.altran.iot.search.LuceneIndexer;
import com.altran.iot.search.LuceneSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class InsideController {

    private static final Logger log = LoggerFactory.getLogger(InsideController.class);

    private final QueryOperations queryOperations;
    private final WriteOperations writeOperations;
    private final ObjectMapper mapper;
    private final LuceneIndexer index;
    private LuceneSearch luceneSearch;


    public static final String HEADING = "message";

    public static final String PATIENT_1 = "patient1";
    public static final String PATIENT_2 = "patient2";
    public static final String PATIENT_3 = "patient3";
    public static final String PATIENT_4 = "patient4";
    public static final String PATIENT_5 = "patient5";

    public static final String CORNER_1 = "corner1";
    public static final String CORNER_2 = "corner2";
    public static final String CORNER_3 = "corner3";
    public static final String CORNER_4 = "corner4";

    private String patient1 = "001BC50C71000019";
    private String patient2 = "001BC50C7100001E";
    private String patient3 = "001BC50C71000017";
    private String patient4 = "001BC50C7100001F";
    private String patient5 = "001BC50C7100001A";
    private String namepatient1 = "Lise";
    private String namepatient2 = "Petter";
    private String namepatient3 = "Ole";
    private String namepatient4 = "Knut";
    private String namepatient5 = "Reidun";

    Random randomGenerator = new Random();

    /**
     * @Autowired public ObservedMethodsResouce(QueryOperations queryOperations, WriteOperations writeOperations, ObjectMapper mapper) {
     * this.queryOperations = queryOperations;
     * this.writeOperations = writeOperations;
     * this.mapper = mapper;
     * }
     */
    @Autowired
    public InsideController(ObservationsService observationsService, ObjectMapper mapper, LuceneIndexer index) {
        this.queryOperations = observationsService;
        this.writeOperations = observationsService;
        this.mapper = mapper;
        this.index = index;
        this.luceneSearch = new LuceneSearch(index.getDirectory());
    }


    @RequestMapping("/dement")
    public ModelAndView calculatePatients() {

        String valuePatient1 = getTriangulationString(patient1);
        String valuePatient2 = getTriangulationString(patient2);
        String valuePatient3 = getTriangulationString(patient3);
        String valuePatient4 = getTriangulationString(patient4);
        String valuePatient5 = getTriangulationString(patient5);


        Map model = new HashMap<String, String>();
        model.put(HEADING, "Pasientovervåkning - demente");


        if (isLost(valuePatient1)) {
            model.put(PATIENT_1, namepatient1 + " <font color=\"orange\">Unknown - " + valuePatient1 + "  - " + patient1 + "</font>");
        } else if (isInside(valuePatient1)) {
            model.put(PATIENT_1, namepatient1 + " Inside - " + valuePatient1 + "  - " + patient1);
        } else {
            model.put(PATIENT_1, namepatient1 + " <font color=\"red\">Outside - " + valuePatient1 + "  - " + patient1 + "</font>");
        }

        if (isLost(valuePatient2)) {
            model.put(PATIENT_2, namepatient2 + " <font color=\"orange\">Unknown - " + valuePatient2 + "  - " + patient2 + "</font>");
        } else if (isInside(valuePatient2)) {
            model.put(PATIENT_2, namepatient2 + " Inside - " + valuePatient2 + "  - " + patient2);
        } else {
            model.put(PATIENT_2, namepatient2 + " <font color=\"red\">Outside - " + valuePatient2 + "  - " + patient2 + "</font>");
        }

        if (isLost(valuePatient3)) {
            model.put(PATIENT_3, namepatient3 + " <font color=\"orange\">Unknown - " + valuePatient3 + "  - " + patient3 + "</font>");
        } else if (isInside(valuePatient3)) {
            model.put(PATIENT_3, namepatient3 + " Inside - " + valuePatient3 + "  - " + patient3);
        } else {
            model.put(PATIENT_3, namepatient3 + " <font color=\"red\">Outside - " + valuePatient3 + "  - " + patient3 + "</font>");
        }

        if (isLost(valuePatient4)) {
            model.put(PATIENT_4, namepatient4 + " <font color=\"orange\">Unknown - " + valuePatient4 + "  - " + patient4 + "</font>");
        } else if (isInside(valuePatient4)) {
            model.put(PATIENT_4, namepatient4 + " Inside - " + valuePatient4 + "  - " + patient4);
        } else {
            model.put(PATIENT_4, namepatient4 + " <font color=\"red\">Outside - " + valuePatient4 + "  - " + patient4 + "</font>");
        }

        if (isLost(valuePatient5)) {
            model.put(PATIENT_5, namepatient5 + " <font color=\"orange\">Unknown - " + valuePatient5 + "  - " + patient5 + "</font>");
        } else if (isInside(valuePatient5)) {
            model.put(PATIENT_5, namepatient5 + " Inside - " + valuePatient5 + "  - " + patient5);
        } else {
            model.put(PATIENT_5, namepatient5 + " <font color=\"red\">Outside - " + valuePatient5 + "  - " + patient5 + "</font>");
        }



        model.put(CORNER_1, "A: 50, B: 30, C:45");
        model.put(CORNER_2, "A: 50, B: 80, C:45");
        model.put(CORNER_3, "A: 40, B: 30, C:15");
        model.put(CORNER_4, "A: 40, B: 80, C:15");


        String message = "Pasientovervåkning - demente";
        return new ModelAndView("dement", "model", model);
    }

    private String getTriangulationString(String patient) {
        String valuePatient1;
        try {
            List<Observation> observations = luceneSearch.search(patient);
            Observation o = observations.get(0);
            valuePatient1 = "(A:" + o.getMeasurements().get("lb") + ", B: 30, C:45)";

        } catch (Exception e) {
            valuePatient1 = "(A: ?, B: ?, C:?) ";
        }
        return valuePatient1;
    }

    private boolean isInside(String position) {
        if (randomGenerator.nextInt(100) > 40) {
            return false;
        }
        return true;
    }

    public static boolean isLost(String position) {
        try {
            String delims = ",";
            StringTokenizer st = new StringTokenizer(position, delims);
            while (st.hasMoreElements()) {
                String value = st.nextElement().toString();
                value = value.replaceAll("[^\\d.]", "");
                Integer i = Integer.parseInt(value);
                if (i < 60) {
                    return true;

                }
            }
        } catch (Exception e) {
            log.error("Error trying to calculate if patient is lost");
            return true;
        }
        return false;
    }
}
package com.clicktime.model.service.calendario;

import com.clicktime.model.ServiceLocator;
import com.clicktime.model.entity.DiaAtendimento;
import com.clicktime.model.entity.DiaAtendimentoResumo;
import com.clicktime.model.entity.Profissional;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Edgard Lopes <edgard-rodrigo@hotmail.com>
 */
public class CalendarioService {

    public static final String MONTH_AS_STRING = "monthAsString";
    public static final String DAYS_OF_MONTH = "weekList";

    private int year;
    private int month;
    private Profissional profissional;
    private List<DiaAtendimentoResumo> resumoList;
    private boolean isProfissional;

    private Map<String, Object> informationsOfMonth;

    public CalendarioService(int year, int month, Profissional profissional, boolean isProfissional) {
        this.year = year;
        this.month = month;
        this.profissional = profissional;
        this.isProfissional = isProfissional;
    }

    public Map<String, Object> getInformations() {
        DateTime dt = new DateTime(year, month, 1, 0, 0);
        DateTime aux = new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0);

        DiaAtendimento da = new DiaAtendimento();
        da.setData(dt);
        da.setProfissional(profissional);

        if (isProfissional) {
            readReportFromMonth(da);
        }
        informationsOfMonth = new HashMap<>();
        informationsOfMonth.put(MONTH_AS_STRING, dt.monthOfYear().getAsText());

        List<List> weekList = new ArrayList<>();
        List<Day> week = new ArrayList<>();
        dt = dt.minusDays(1);
        Day day = new Day(dt);

        while (dt.getMillis() <= aux.dayOfMonth().withMaximumValue().getMillis()) {
            dt = dt.plusDays(1);

            while (dt.getDayOfWeek() < 7 && dt.getMillis() <= aux.dayOfMonth().withMaximumValue().getMillis()) {
                day = new Day(dt);
                day.setResumo(containsDay(dt));
                week.add(day);
                dt = dt.plusDays(1);
            }

            if (week.size() > 0) {
                weekList.add(week);
            }
            week = new ArrayList<>();
            day = new Day(dt);
            day.setResumo(containsDay(dt));

            if (dt.getDayOfMonth() == aux.dayOfMonth().withMaximumValue().getDayOfMonth() && weekList.get(weekList.size() - 1).size() < 7) {
                day = new Day(dt);
                day.setResumo(containsDay(dt));
                weekList.get(weekList.size() - 1).add(day);
            }

            week.add(day);
        }

        int diasFaltantes = 7 - weekList.get(0).size();
        if (diasFaltantes > 0) {
            for (int i = 0; i < diasFaltantes; i++) {
                Day day1 = new Day();
                day1.setDay(null);
                day1.setDayOfWeekShort("");
                weekList.get(0).add(0, day1);
            }
        }

        diasFaltantes = 7 - weekList.get(weekList.size() - 1).size();
        if (diasFaltantes > 0) {
            for (int i = 0; i < diasFaltantes; i++) {
                Day day1 = new Day();
                day1.setDay(null);
                day1.setDayOfWeekShort("");
                weekList.get(weekList.size() - 1).add(weekList.get(weekList.size() - 1).size(), day1);
            }
        }

        informationsOfMonth.put(DAYS_OF_MONTH, weekList);

        informationsOfMonth.put("month", month);
        informationsOfMonth.put("year", year);

        DateTime date = new DateTime(year, month, 1, 0, 0);
        date = date.plusMonths(1);
        informationsOfMonth.put("nextMonth", date);

        date = date.minusMonths(2);
        informationsOfMonth.put("previousMonth", date);

        date = new DateTime();
        informationsOfMonth.put("currentMonth", date);

        return informationsOfMonth;
    }

    public List<DiaAtendimentoResumo> readReportFromMonth(DiaAtendimento da) {
        resumoList = new ArrayList<DiaAtendimentoResumo>();
        try {
            resumoList = ServiceLocator.getDiaAtendimentoResumoService().reportFromMonth(da);
        } catch (Exception ex) {
            Logger.getLogger(CalendarioService.class.getName()).log(Level.SEVERE, null, ex);
        }

        return resumoList;
    }

    public DiaAtendimentoResumo containsDay(DateTime dt) {

        if (resumoList != null) {
            for (DiaAtendimentoResumo d : resumoList) {
                if (d.getDiaAtendimento().getData().getDayOfMonth() == dt.getDayOfMonth()) {
                    return d;
                }
            }
        }
        return null;
    }

    public Map<String, String> parseJsonToFeriadoMap(String jsonFeriados) {
        List<Map> feriados = new ArrayList<Map>();

        Map<String, String> feriadoMap = new HashMap<String, String>();
        if (!jsonFeriados.equals("-1")) {
            Gson g = new Gson();
            Class c = new HashMap<String, String>().getClass();
            Map<String, List> map = (Map<String, List>) g.fromJson(jsonFeriados, c);
            feriados = map.get("holidays");
            for (Map feriado : feriados) {
                feriadoMap.put((String) feriado.get("date"), (String) feriado.get("name"));
            }
        }

        return feriadoMap;
    }

    public static DateTime parseStringToDateTime(String dateTimeStr, String pattern) throws Exception {
        DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
        return formatter.parseDateTime(dateTimeStr);

    }

    public static DateTime getFirstDayOfWeek(DateTime day) {
        if (day.getDayOfWeek() == 7) {
            return day;
        } else {
            return day.withDayOfWeek(1).minusDays(1);
        }
    }

    public static DateTime getLastDayOfWeek(DateTime day) {
        if (day.getDayOfWeek() == 7) {
            return day.plusDays(1).withDayOfWeek(6);
        } else {
            return day.withDayOfWeek(6);
        }
    }

}

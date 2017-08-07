<%@tag description="Apresenta hora no formato HH:mm" pageEncoding="UTF-8"%>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>


<%-- The list of normal or fragment attributes can be specified here: --%>
<%@attribute name="time" required="true" type="org.joda.time.DateTime"%>

<%-- any content can be specified here e.g.: --%>
<joda:format value="${time}" pattern="HH:mm"/>

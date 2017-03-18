<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>


<input type="hidden" name="execucaoID" value="${execucao.id}" id="execucaoID">
<!--<ul class="list-group col-lg-6" >-->
    <c:forEach var="horario" items="${horarioList}">
        <button type="button" class="opcao-horario btn btn-success btn-block" role="button">
            <input type="hidden" value="${horario.IDList}" class="idList">
            <!--<p>-->
                <span><joda:format value="${horario.horarioAtendimento.horaInicio}" pattern="HH:mm"/> - <joda:format value="${horario.horarioAtendimento.horaFim}" pattern="HH:mm"/></span>
            <!--</p>-->
        </button>
    </c:forEach>
<!--</ul>-->
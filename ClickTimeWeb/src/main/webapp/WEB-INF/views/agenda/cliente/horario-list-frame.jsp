<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="t" tagdir="/WEB-INF/tags/"%>


<input type="hidden" name="execucaoID" value="${execucao.id}" id="execucaoID" >

<c:forEach var="horario" items="${horarioList}">
    <button type="button" class="opcao-horario btn btn-success btn-block" role="button">
        <input type="hidden" value="${horario.IDList}" class="idList">
        <span>
            <t:time time="${horario.horarioAtendimento.horaInicio}"/>
                - 
            <t:time time="${horario.horarioAtendimento.horaFim}"/>
        </span>
    </button>
</c:forEach>
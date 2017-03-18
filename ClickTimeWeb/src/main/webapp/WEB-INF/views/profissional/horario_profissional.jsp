<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<div class="form-group col-lg-6 col-sm-6">
    <label for="horaInicio" class="hidden-xs control-label">Inicio do expediente <span class="text-danger">*</span></label>
    <input type="text" class="form-control time campoHora" id="horaInicio" name="horaInicio" placeholder="hh:mm" value="<joda:format value="${usuario.horaInicio}" pattern="HH:mm"/>" >
    <c:if test="${errors.horaInicio != null}"><p class="text-danger">${errors.horaInicio}</p></c:if>
</div>

<div class="form-group col-lg-6 col-sm-6">
    <label for="horaFim" class="hidden-xs control-label">Fim do expediente <span class="text-danger">*</span></label>
    <input type="text" class="form-control time campoHora" id="horaFim" name="horaFim" placeholder="hh:mm" value="<joda:format pattern="HH:mm" value="${usuario.horaFim}"/>">
    <c:if test="${errors.horaFim != null}"><p class="text-danger">${errors.horaFim}</p></c:if>
</div>

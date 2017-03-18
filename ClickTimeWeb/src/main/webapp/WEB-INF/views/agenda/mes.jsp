<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="joda" uri="http://www.joda.org/joda/time/tags" %>

<c:if test="${not empty profissional}">
    <c:set var="profissionalURL" value="/${profissional.nomeUsuario}"/>
</c:if>

<c:if test="${execucaoSelected != null}">
    <c:set var="servico" value="?servicoID=${execucaoSelected.id}"/>
</c:if>


<div class="fc-calendar
     <c:if test="${fn:length(weekList) eq 6}"> fc-six-rows</c:if> 
     <c:if test="${fn:length(weekList) eq 4}"> fc-four-rows</c:if> 
     <c:if test="${fn:length(weekList) eq 5}"> fc-five-rows</c:if>
     <c:if test="${usuarioLogado['class'].name eq 'clicktime.model.entity.Usuario'}">calendario-cliente</c:if>">

         <div class="fc-head">
             <div class="day-of-week">Domingo</div>
             <div class="day-of-week-short">Dom.</div>
             <div class="day-of-week">Segunda</div>
             <div class="day-of-week-short">Seg.</div>
             <div class="day-of-week">Terça</div>
             <div class="day-of-week-short">Ter.</div>
             <div class="day-of-week">Quarta</div>
             <div class="day-of-week-short">Qua.</div>
             <div class="day-of-week">Quinta</div>
             <div class="day-of-week-short">Qui.</div>
             <div class="day-of-week">Sexta</div>
             <div class="day-of-week-short">Sex.</div>
             <div class="day-of-week">Sabado</div>
             <div class="day-of-week-short">Sab.</div>
         </div>
         <div class="fc-body">
         <c:forEach var="week" items="${weekList}">
             <div class="fc-row">
                 <c:forEach var="day" items="${week}">
                     <div class="<c:if test="${empty day.day}"> empty-day</c:if> ">
                         <joda:format value="${day.day}" pattern="d" var="dia"/>
                         <a href="<c:url value="${profissionalURL}/agenda/${year}/${month}/${dia}${servico}"/>" role="button">
                             <div class="day" >

                                 <span class="fc-date">${dia}</span>
                                 <span class="fc-weekday">${day.dayOfWeekShort}</span>
                                 <div class="info-feriado hidden">
                                     <button type="button" class="btn btn-xs btn-primary btn-info-dia btn-feriado" data-container="body" data-toggle="popover" data-placement="top" data-content="">
                                         <span class="glyphicon glyphicon-star"></span>
                                     </button> 
                                 </div>
                                 <c:if test="${not empty day.day}">

                                     <div class="day-info-mobile">                                                
                                         <c:if test="${not empty day.resumo}">
                                             <div>
                                                 <span>Horarios disponiveis: ${day.resumo.horariosLivres}</span>
                                                 <%--<c:if test="${empty profissional}"><span>B: ${day.resumo.horariosBloqueados}</span></c:if>--%>
                                                 </div>
                                         </c:if>
                                     </div>
                                     <div class="day-info">
                                         <c:if test="${not empty day.resumo}">
                                             <div class="info-dia">
                                                 <button type="button" class="btn btn-xs btn-primary btn-info-dia" data-container="body" data-toggle="popover" data-placement="left" data-content="Horários livres: ${day.resumo.horariosLivres}">
                                                     <span class="glyphicon glyphicon-info-sign"></span>
                                                 </button> 
                                             </div>
                                         </c:if>                                         
                                     </div>
                                 </c:if>
                             </div>
                         </a>
                     </div>
                 </c:forEach>
             </div>
         </c:forEach>
     </div>
</div>

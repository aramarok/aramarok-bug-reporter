<html xmlns:f="http://java.sun.com/jsf/core"
      xmlns:h="http://java.sun.com/jsf/html"
      xmlns:ui="http://java.sun.com/jsf/facelets">
      
	<body bgcolor="white">
		<ui:composition template="./WEB-INF/layout/layout.jsp">
			<ui:define name="title">
				#{general['help.title']}
			</ui:define>
			
			<ui:define name="horizontalMenu">
			</ui:define>
			
			<ui:define name="body">
				<p class="P-align-center">
					<h:outputText value="#{general['help.title']}" styleClass="title"/>
				</p>
				
				<br />
				<a href="javascript:history.back(1)" >
					<h:outputText value="#{general['help.back']}"/>
				</a>
				
				
				
<div class="NAVHEADER">
<table summary="Header navigation table" border="0" cellpadding="0" cellspacing="0" width="100%">
<tbody><tr><th colspan="3" align="center">aramarok - bug reporter</th></tr><tr>
<td align="left" valign="bottom" width="10%">
</td><td align="center" valign="bottom" width="80%">
</td><td align="right" valign="bottom" width="10%">

</td></tr></tbody></table>
</div>

<div class="CHAPTER">

<h1><a name="USERGUIDE">Capitolul 1. Ghidul utilizatorului</a></h1>

<div class="TOC"><dl><dt><a name="USERGUIDE">

<b>Continutul</b></a></dt><dt><a name="USERGUIDE">1.1. </a><a href="#PRIMER">Introducere</a></dt><dd><dl><dt>1.1.1. 
<a href="#CREATEACCOUNT">Crearea unui cont</a></dt><dt>1.1.2. <a href="#ENTERBUG">Introducerea unui bug nou</a></dt></dl></dd>
<dt>1.2. <a href="#BUGSEARCH">Cautarea in baza de date</a></dt>
<dt>1.3. <a href="#BUGDETAIL">Detalii despre buguri</a></dt></dl>

</div>
</div>
<blockquote class="ABSTRACT">

<div class="ABSTRACT"><a name="AEN8"></a><p><a name="AEN8"></a></p>

</div>

</blockquote>

<div class="SECT1"><h1 class="SECT1"><a name="PRIMER">1.1. Introducere</a></h1>

<div class="SECT2">

<h2 class="SECT2"><a name="CREATEACCOUNT">1.1.1. Crearea unui cont</a></h2><p>
<a name="CREATEACCOUNT">Inregistrarea unui cont not in sistem se poate face folosind linkul <span class="bold"><b class="EMPHASIS">Creare cont nou</b></span>.
Pentru inregistrarea unui cont nou, va fi necesar introducerea unui nume de utilizator valid, a unei adrese de e-mail valide si a unei parole
Specificarea celorlalte date, precum numele, prenumele, etc.., sunt optionale.
Dupa inregistrarea unui cont nou, utilizatorul va avea rolul de Guest, pana in momentul in care un Administrator va schimba rolul acestuia.
Odata ce contul a fost inregistrat in sistem, utilizatorul se va putea logina folosind numele de utilizator si parola specificate.
</a></p>

</div>

<div class="SECT2"><h2 class="SECT2">
<a name="ENTERBUG">1.1.2. Introducerea unui bug nou</a></h2><p><a name="ENTERBUG">
Odata autentificat in sistem, si avand drepturile necesare, se poate raporta un bug nou accesand linkul<span class="bold">
<b class="EMPHASIS">Bug nou</b></span>
din meniul din partea superioara a paginii. Din urmatoarea pagina se va alege proiectul pentru care se introduce bugul, acesta fiind ales prin clic pe el si continuare la urmatoarea pagina.
</a></p>

</div>

<div class="MEDIAOBJECT"><p><a name="ENTERBUG">
<img src="./images/enterbug-chooseproject.jpg" /></a></p>

</div>

<div class="CAPTION"><p>
<a name="ENTERBUG">Selectarea unui produs pentru raportarea unui bug.</a></p>

</div>

<p></p>

</div>

<p>
<a name="ENTERBUG">Odata ce un produs a fost selectat se va trece la urmatoare pagina. <span class="bold">
<b class="EMPHASIS">Componenta</b></span> si <span class="bold">
<b class="EMPHASIS">versiunea</b></span> sunt campuri care va permit specificarea detaliilor despre componenta si versiunea a componentei in momentul in care a aparut bugul.
Campul <span class="bold"><b class="EMPHASIS">Titlu</b></span> este obligatoriu, aici introducandu-se o scurta descriere a bugului aparut, in timp ce campul<span class="bold"><b class="EMPHASIS">Descriere</b></span>
va include detalierea erorii, si modul de manifestare a ei, eventual pasii necesari de parcurs pentru reproducerea erorii.
Pentru bugul introdus se vor specifica detaliile despre platforma pe care s-a observat, sistemul de operare, etc.

 <div class="MEDIAOBJECT"><p><a name="ENTERBUG">
 <img src="./images/enterbug-detail.jpg" />
 </a>
 </p>
 
 </div>
 
 
 <div class="CAPTION"><p>
 <a name="ENTERBUG">Raportarea unui bug.</a>
 </p>
 
 </div>
 
</a>
<p>
</p>


<p>
<a name="ENTERBUG">Aceasta este tot! Bugul a fost introdus si este gata sa fie vazut de si reparat de catre dezvoltatori.</a></p>

 
 
 <br />
 <br />
 
 <div class="SECT1"><h1 class="SECT1"><a name="#BUGSEARCH">1.2. Cautarea in baza de date</a></h1><p>
 <a name="BUGSEARCH">
 Pentru a acesa pagina de cautare, se va urma linkul <span class="bold"><b class="EMPHASIS">Cautare</b></span> din meniul din partea superioara a aplicatiei.
Accesand aceasta pagina, se permite cautarea avansata a bugurilor din baza de date. Se pot bifa diferite checkboxuri pentru precizarea parametrilor cautarii, sau selectarea unor item-uri din liste.
</a></p>

<div class="MEDIAOBJECT"><p><a name="BUGSEARCH"><img src="./images/query.jpg" /></a></p>

<div class="CAPTION"><p>
<a name="BUGSEARCH">Pagina de cautare dupa buguri.</a></p>

</div>

<p></p>

</div>

<p>
<a name="BUGSEARCH" />
Odata ce a fost efectuata o cautare, rezultatul cautarii va aparea in aceeasi pagina, in partea de jos.
Un filtru de cautare poate sa fie salvat, ca ulterior sa se filtreze aceleasi buguri, sau buguri noi care au aceleasi proprietati.
Pentru a vizualiza filtrele salvate existente, se va accesa linkul <span class="bold">
<b class="EMPHASIS">Filtre salvate</b></span>  din partea superioara a paginii.
</p>

</div>


<br />
 <br />
 

<div class="SECT1"><h1 class="SECT1"><a name="#BUGDETAIL">1.3. Detalii despre buguri</a></h1><p><a name="BUGDETAIL">
Pagina de vizualizare a bugurilor existente in sistem va ofera o privire cat se poate de detaliata asupra bugului selectat.
Din aceasta pagina, cei care au dreptul, vor putea modifica majoritatea detaliilor despre un bug exitent in baza de date.
</a></p>

<div class="MEDIAOBJECT"><p><a name="BUGDETAIL"><img src="./images/bugdetail.jpg" /></a></p>

<div class="CAPTION"><p><a name="BUGDETAIL">Vizualizarea/editarea unui bug.</a></p>

</div>

<p></p>

</div>

<p><a name="BUGDETAIL">
In momentul in care sunt facute modificari asupra unui bug, este pastrat un istoric al acestor modificari in parte pe fiecare bug.
</a></p>
<p><a name="BUGDETAIL">Din pagina de editare bug, vor putea edita detaliile unui bug, adauga comentarii sau vota comentariile utile.
Pentru vizualizarea actiunilor asupra unui bug, va trebui accesat linkul <span class="GUILABEL">Vizualizeaza activitatea bugului</span>
. Facand acest lucuru, intr-o noua pagina vor aparea informatiile cu privire la modificarile ce au fost facute unui bug, precum si detalii ca: data, utilizator, status bug, detalii modificari survenite.
</a></p>
<p><a name="BUGDETAIL">Adaugarea comentariilor la buguri, va permite detalierea modului de aparitie a erorii, astfel ca daca alti utilizatori vor observa aceeasi eroare, isi vor putea scrie aici parerea.
Comentariile introduse vor putea fi votate de catre utilizatori, astfel incat cele utile sa iasa in evidenta.</a></p>

</div>
			</p>
			
			<br />
				<a href="javascript:history.back(1)" >
					<h:outputText value="#{general['help.back']}"/>
				</a>	
				
			</ui:define>
		</ui:composition>
	</body>
</html>
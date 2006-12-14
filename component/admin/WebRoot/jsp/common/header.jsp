<%@ taglib prefix="tab" uri="http://ditchnet.org/jsp-tabs-taglib"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="h" uri="http://java.sun.com/jsf/html"%>


</style>

<f:subview id="header">

    <h:form>
        <h:panelGrid id="header-toolbar-grid" columns="9"
            cellpadding="4" cellspacing="0" border="0"
            styleClass="toolbar-header" rowClasses="toolbar-header">


            <h:outputText value="Dashboard:" styleClass="toolbar-text" />

            <h:commandLink action="inbox">
                <h:graphicImage url="/images/inbox.gif"
                    styleClass="toolbar-icon" alt="Inbox" />
                <h:outputText value="Inbox" styleClass="toolbar-command" />
            </h:commandLink>

            <h:commandLink id="zonecl"
                actionListener="#{sessionBean.changeLight}">
                <h:outputText value="Zone" styleClass="toolbar-command" />
            </h:commandLink>

            <h:commandLink action="Security">
                <h:outputText value="Security"
                    styleClass="toolbar-command" />
            </h:commandLink>

            <h:commandLink action="Alerts">
                <h:graphicImage url="/images/create.gif"
                    styleClass="toolbar-icon" alt="TBD" />
                <h:outputText value="Alerts"
                    styleClass="toolbar-command" />
            </h:commandLink>

            <h:commandLink action="Workflow">
                <h:graphicImage url="/images/create.gif"
                    styleClass="toolbar-icon" alt="TBD" />
                <h:outputText value="Workflow"
                    styleClass="toolbar-command" />
            </h:commandLink>

            <h:commandLink action="logout">
                <h:graphicImage url="/images/logout.gif"
                    styleClass="toolbar-icon" alt="Logout" />
                <h:outputText value="Logout"
                    styleClass="toolbar-command" />
            </h:commandLink>

            <h:outputText value="" styleClass="toolbar-user" />
            <h:commandLink action="showuserdetails">
                <h:graphicImage url="/images/person.png"
                    styleClass="toolbar-icon" alt="Logout" height="24"
                    width="24" />
                <h:outputText value="#{authenticationBean.authUsername}"
                    styleClass="toolbar-user" />
            </h:commandLink>


        </h:panelGrid>

    </h:form>
    <f:subview>
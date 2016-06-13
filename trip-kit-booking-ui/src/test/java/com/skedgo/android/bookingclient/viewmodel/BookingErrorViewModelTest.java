package com.skedgo.android.bookingclient.viewmodel;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.BuildConfig;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.RuntimeEnvironment;

import io.fabric.sdk.android.Fabric;

import static org.assertj.core.api.Assertions.assertThat;

public class BookingErrorViewModelTest {

  private BookingErrorViewModel viewModel;
  private String defaultErrorTitle;

  @Before
  public void setUp() {

    defaultErrorTitle = "An awesome error occurred!";
    viewModel = new BookingErrorViewModel(new Gson(), defaultErrorTitle);
  }

  @Test
  public void shouldReadTitleAndMessageForError() {
    viewModel.read("{\"errorCode\":451,\"title\":\"No quotes\",\"error\":\"Cannot find any available vehicles\"}");
    assertThat(viewModel.getErrorTitle()).isEqualTo("No quotes");
    assertThat(viewModel.getErrorMessage()).isEqualTo("Cannot find any available vehicles");
  }

  @Test
  public void shouldUseDefaultErrorTitle() {
    final String errorJson = "{\n" +
        "  \"input\": [\n" +
        "    {\n" +
        "      \"value\": {\n" +
        "        \"address\": \"Martin Place \\u0026 George Street\",\n" +
        "        \"name\": \"Martin Place \\u0026 George Street\",\n" +
        "        \"lat\": -33.86749,\n" +
        "        \"lng\": 151.20699\n" +
        "      },\n" +
        "      \"id\": \"from\",\n" +
        "      \"title\": \"From\",\n" +
        "      \"type\": \"address\",\n" +
        "      \"hidden\": false,\n" +
        "      \"readOnly\": false,\n" +
        "      \"required\": true\n" +
        "    },\n" +
        "    {\n" +
        "      \"value\": {\n" +
        "        \"address\": \"Crown Street \\u0026 Fouveaux Street - Crown Street \\u0026 Fouveaux Street - Crown Street\",\n" +
        "        \"name\": \"Crown Street \\u0026 Fouveaux Street - Crown Street \\u0026 Fouveaux Street - Crown Street\",\n" +
        "        \"lat\": -33.88546753643035,\n" +
        "        \"lng\": 151.21398308982234\n" +
        "      },\n" +
        "      \"id\": \"to\",\n" +
        "      \"title\": \"To\",\n" +
        "      \"type\": \"address\",\n" +
        "      \"hidden\": false,\n" +
        "      \"readOnly\": false,\n" +
        "      \"required\": true\n" +
        "    },\n" +
        "    {\n" +
        "      \"maxValue\": 20,\n" +
        "      \"minValue\": 1,\n" +
        "      \"value\": 1,\n" +
        "      \"id\": \"passengers\",\n" +
        "      \"title\": \"Passenger(s)\",\n" +
        "      \"type\": \"stepper\",\n" +
        "      \"hidden\": false,\n" +
        "      \"readOnly\": false,\n" +
        "      \"required\": false\n" +
        "    },\n" +
        "    {\n" +
        "      \"value\": 1444720070,\n" +
        "      \"id\": \"dateTime\",\n" +
        "      \"title\": \"DateTime\",\n" +
        "      \"type\": \"datetime\",\n" +
        "      \"hidden\": false,\n" +
        "      \"readOnly\": false,\n" +
        "      \"required\": true\n" +
        "    }\n" +
        "  ]\n" +
        "}";
    viewModel.read(errorJson);
    assertThat(viewModel.getErrorTitle()).isEqualTo(defaultErrorTitle);
    assertThat(viewModel.getErrorMessage()).isNull();
  }

  @Test
  public void shouldAlsoUseDefaultErrorTitle() {
    final String errorHtml = "\n" +
        "<html><head><title>Apache Tomcat/7.0.54 - Error report</title><style><!--H1 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:22px;} H2 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:16px;} H3 {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;font-size:14px;} BODY {font-family:Tahoma,Arial,sans-serif;color:black;background-color:white;} B {font-family:Tahoma,Arial,sans-serif;color:white;background-color:#525D76;} P {font-family:Tahoma,Arial,sans-serif;background:white;color:black;font-size:12px;}A {color : black;}A.name {color : black;}HR {color : #525D76;}--></style> </head><body><h1>HTTP Status 500 - org.apache.cxf.interceptor.Fault</h1><HR size=\"1\" noshade=\"noshade\"><p><b>type</b> Exception report</p><p><b>message</b> <u>org.apache.cxf.interceptor.Fault</u></p><p><b>description</b> <u>The server encountered an internal error that prevented it from fulfilling this request.</u></p><p><b>exception</b> <pre>java.lang.RuntimeException: org.apache.cxf.interceptor.Fault\n" +
        "    org.apache.cxf.interceptor.AbstractFaultChainInitiatorObserver.onMessage(AbstractFaultChainInitiatorObserver.java:116)\n" +
        "    org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:371)\n" +
        "    org.apache.cxf.transport.ChainInitiationObserver.onMessage(ChainInitiationObserver.java:121)\n" +
        "    org.apache.cxf.transport.http.AbstractHTTPDestination.invoke(AbstractHTTPDestination.java:243)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invokeDestination(ServletController.java:223)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:197)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:149)\n" +
        "    org.apache.cxf.transport.servlet.CXFNonSpringServlet.invoke(CXFNonSpringServlet.java:171)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.handleRequest(AbstractHTTPServlet.java:290)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.doPost(AbstractHTTPServlet.java:209)\n" +
        "    javax.servlet.http.HttpServlet.service(HttpServlet.java:646)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.service(AbstractHTTPServlet.java:265)\n" +
        "    com.buzzhives.tomcat.CorsFilter.handleSimpleCORS(CorsFilter.java:309)\n" +
        "    com.buzzhives.tomcat.CorsFilter.doFilter(CorsFilter.java:177)\n" +
        "    com.buzzhives.tomcat.FilterProxy.doFilter(FilterProxy.java:65)\n" +
        "    com.buzzhives.tomcat.LogRequestFilter.doFilter(LogRequestFilter.java:47)\n" +
        "</pre></p><p><b>root cause</b> <pre>org.apache.cxf.interceptor.Fault\n" +
        "    org.apache.cxf.service.invoker.AbstractInvoker.createFault(AbstractInvoker.java:163)\n" +
        "    org.apache.cxf.service.invoker.AbstractInvoker.invoke(AbstractInvoker.java:129)\n" +
        "    org.apache.cxf.jaxrs.JAXRSInvoker.invoke(JAXRSInvoker.java:200)\n" +
        "    org.apache.cxf.jaxrs.JAXRSInvoker.invoke(JAXRSInvoker.java:99)\n" +
        "    org.apache.cxf.interceptor.ServiceInvokerInterceptor$1.run(ServiceInvokerInterceptor.java:59)\n" +
        "    org.apache.cxf.interceptor.ServiceInvokerInterceptor.handleMessage(ServiceInvokerInterceptor.java:96)\n" +
        "    org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:307)\n" +
        "    org.apache.cxf.transport.ChainInitiationObserver.onMessage(ChainInitiationObserver.java:121)\n" +
        "    org.apache.cxf.transport.http.AbstractHTTPDestination.invoke(AbstractHTTPDestination.java:243)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invokeDestination(ServletController.java:223)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:197)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:149)\n" +
        "    org.apache.cxf.transport.servlet.CXFNonSpringServlet.invoke(CXFNonSpringServlet.java:171)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.handleRequest(AbstractHTTPServlet.java:290)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.doPost(AbstractHTTPServlet.java:209)\n" +
        "    javax.servlet.http.HttpServlet.service(HttpServlet.java:646)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.service(AbstractHTTPServlet.java:265)\n" +
        "    com.buzzhives.tomcat.CorsFilter.handleSimpleCORS(CorsFilter.java:309)\n" +
        "    com.buzzhives.tomcat.CorsFilter.doFilter(CorsFilter.java:177)\n" +
        "    com.buzzhives.tomcat.FilterProxy.doFilter(FilterProxy.java:65)\n" +
        "    com.buzzhives.tomcat.LogRequestFilter.doFilter(LogRequestFilter.java:47)\n" +
        "</pre></p><p><b>root cause</b> <pre>java.lang.NullPointerException\n" +
        "    com.buzzhives.Routing.booking.providers.taxi.cabforce.BookingSearchResponse.getResultData(BookingSearchResponse.java:17)\n" +
        "    com.buzzhives.SatAppServer.booking.taxi.cabforce.CabforceBooking.step1Search(CabforceBooking.java:128)\n" +
        "    com.buzzhives.SatAppServer.booking.taxi.cabforce.CabforceBooking.attemptBooking(CabforceBooking.java:66)\n" +
        "    com.buzzhives.SatAppServer.booking.taxi.cabforce.CabforceBooking.attemptBooking(CabforceBooking.java:58)\n" +
        "    com.buzzhives.SatAppServer.BookingServiceImpl.startBooking(BookingServiceImpl.java:152)\n" +
        "    sun.reflect.GeneratedMethodAccessor640.invoke(Unknown Source)\n" +
        "    sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)\n" +
        "    java.lang.reflect.Method.invoke(Method.java:606)\n" +
        "    org.apache.cxf.service.invoker.AbstractInvoker.performInvocation(AbstractInvoker.java:181)\n" +
        "    org.apache.cxf.service.invoker.AbstractInvoker.invoke(AbstractInvoker.java:97)\n" +
        "    org.apache.cxf.jaxrs.JAXRSInvoker.invoke(JAXRSInvoker.java:200)\n" +
        "    org.apache.cxf.jaxrs.JAXRSInvoker.invoke(JAXRSInvoker.java:99)\n" +
        "    org.apache.cxf.interceptor.ServiceInvokerInterceptor$1.run(ServiceInvokerInterceptor.java:59)\n" +
        "    org.apache.cxf.interceptor.ServiceInvokerInterceptor.handleMessage(ServiceInvokerInterceptor.java:96)\n" +
        "    org.apache.cxf.phase.PhaseInterceptorChain.doIntercept(PhaseInterceptorChain.java:307)\n" +
        "    org.apache.cxf.transport.ChainInitiationObserver.onMessage(ChainInitiationObserver.java:121)\n" +
        "    org.apache.cxf.transport.http.AbstractHTTPDestination.invoke(AbstractHTTPDestination.java:243)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invokeDestination(ServletController.java:223)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:197)\n" +
        "    org.apache.cxf.transport.servlet.ServletController.invoke(ServletController.java:149)\n" +
        "    org.apache.cxf.transport.servlet.CXFNonSpringServlet.invoke(CXFNonSpringServlet.java:171)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.handleRequest(AbstractHTTPServlet.java:290)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.doPost(AbstractHTTPServlet.java:209)\n" +
        "    javax.servlet.http.HttpServlet.service(HttpServlet.java:646)\n" +
        "    org.apache.cxf.transport.servlet.AbstractHTTPServlet.service(AbstractHTTPServlet.java:265)\n" +
        "    com.buzzhives.tomcat.CorsFilter.handleSimpleCORS(CorsFilter.java:309)\n" +
        "    com.buzzhives.tomcat.CorsFilter.doFilter(CorsFilter.java:177)\n" +
        "    com.buzzhives.tomcat.FilterProxy.doFilter(FilterProxy.java:65)\n" +
        "    com.buzzhives.tomcat.LogRequestFilter.doFilter(LogRequestFilter.java:47)\n" +
        "</pre></p><p><b>note</b> <u>The full stack trace of the root cause is available in the Apache Tomcat/7.0.54 logs.</u></p><HR size=\"1\" ";

    // TODO: crashlitycs exception as it's not initialized, how to mock it?
    //viewModel.read(errorHtml);

    assertThat(viewModel.getErrorTitle()).isEqualTo(defaultErrorTitle);
    assertThat(viewModel.getErrorMessage()).isNull();
  }
}
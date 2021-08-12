package com.baremaps.openapi.services;

import com.baremaps.api.DefaultApi;
import com.baremaps.model.LandingPage;
import com.baremaps.model.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootService implements DefaultApi {

  private static final Logger logger = LoggerFactory.getLogger(RootService.class);

  @Override
  public LandingPage getLandingPage(String host,  String xForwardedHost, String xForwardedProto) {

    LandingPage landingPage = new LandingPage();
//    ApiClient apiClient = this(Configuration.getDefaultApiClient());
//    this.getApiClient().getBasePath();

    logger.info("Listening on {}", super.getApiClient().getBasePath());
//    if (typeof this.host === 'undefined' || this.host === '') {
//      this.host = location.host;
//    }
//    if (location.port) {
//      this.host = this.host + ':' + location.port;
//    }

    landingPage.setTitle("Baremaps");
    landingPage.setDescription("Baremaps OGC API Landing Page");

    String address = "localhost:8080";

    Link linkRoot = new Link();
    linkRoot.title("This document (landing page)");
    linkRoot.setHref(String.format("http://%s/", address));
    linkRoot.setRel("application/json");
    landingPage.getLinks().add(linkRoot);

    Link linkConformance = new Link();
    linkConformance.title("Conformance declaration");
    linkConformance.setHref(String.format("http://%s/conformance", address));
    linkConformance.setRel("application/json");
    landingPage.getLinks().add(linkConformance);

    return landingPage;

  }
}

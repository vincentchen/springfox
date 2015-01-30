package com.mangofactory.documentation.spi.service.contexts;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Ordering;
import com.mangofactory.documentation.schema.AlternateTypeRule;
import com.mangofactory.documentation.schema.WildcardType;
import com.mangofactory.documentation.service.annotations.ApiIgnore;
import com.mangofactory.documentation.service.model.ApiDescription;
import com.mangofactory.documentation.service.model.ApiListingReference;
import com.mangofactory.documentation.service.model.Operation;
import com.mangofactory.documentation.service.model.ResponseMessage;
import com.mangofactory.documentation.service.model.builder.ResponseMessageBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Ordering.*;
import static com.google.common.collect.Sets.*;
import static com.mangofactory.documentation.schema.AlternateTypeRules.*;
import static com.mangofactory.documentation.spi.service.contexts.Orderings.*;
import static java.util.Arrays.asList;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

public class Defaults {

  private HashSet<Class> ignored;
  private LinkedHashMap<RequestMethod, List<ResponseMessage>> responses;
  private List<Class<? extends Annotation>> annotations;
  private Ordering<Operation> operationOrdering;
  private Ordering<ApiDescription> apiDescriptionOrdering;
  private Ordering<ApiListingReference> apiListingReferenceOrdering;

  public Defaults() {
    init();
  }

  public Set<Class> defaultIgnorableParameterTypes() {
    return ignored;
  }

  /**
   * Default response messages set on all api operations
   */
  public Map<RequestMethod, List<ResponseMessage>> defaultResponseMessages() {
    return responses;
  }

  public List<Class<? extends Annotation>> defaultExcludeAnnotations() {
    return annotations;
  }

  public Ordering<Operation> operationOrdering() {
    return operationOrdering;
  }


  public Ordering<ApiDescription> apiDescriptionOrdering() {
    return apiDescriptionOrdering;
  }

  public Ordering<ApiListingReference> apiListingReferenceOrdering() {
    return apiListingReferenceOrdering;
  }

  public List<AlternateTypeRule> defaultRules(TypeResolver typeResolver) {
    List<AlternateTypeRule> rules = newArrayList();
    rules.add(newRule(typeResolver.resolve(Map.class), typeResolver.resolve(Object.class)));
    rules.add(newRule(typeResolver.resolve(Map.class, String.class, Object.class),
            typeResolver.resolve(Object.class)));
    rules.add(newRule(typeResolver.resolve(Map.class, Object.class, Object.class),
            typeResolver.resolve(Object.class)));
    rules.add(newRule(typeResolver.resolve(Map.class, String.class, String.class),
            typeResolver.resolve(Object.class)));
    rules.add(newMapRule(WildcardType.class, WildcardType.class));

    rules.add(newRule(typeResolver.resolve(ResponseEntity.class, WildcardType.class),
            typeResolver.resolve(WildcardType.class)));

    rules.add(newRule(typeResolver.resolve(HttpEntity.class, WildcardType.class),
            typeResolver.resolve(WildcardType.class)));
    return rules;
  }



  private void init() {
    initIgnorableTypes();
    initResponseMessages();
    initExcludeAnnotations();
    initOrderings();
  }

  private void initOrderings() {
    operationOrdering = from(positionComparator()).compound(nickNameComparator());
    apiDescriptionOrdering = from(apiPathCompatator());
    apiListingReferenceOrdering = from(listingPositionComparator()).compound(listingReferencePathComparator());
  }

  private void initExcludeAnnotations() {
    annotations = new ArrayList<Class<? extends Annotation>>();
    annotations.add(ApiIgnore.class);
  }

  private void initIgnorableTypes() {
    ignored = newHashSet();
    ignored.add(ServletRequest.class);
    ignored.add(HttpHeaders.class);
    ignored.add(ServletResponse.class);
    ignored.add(HttpServletRequest.class);
    ignored.add(HttpServletResponse.class);
    ignored.add(HttpHeaders.class);
    ignored.add(BindingResult.class);
    ignored.add(ServletContext.class);
    ignored.add(UriComponentsBuilder.class);
    ignored.add(ApiIgnore.class);
  }

  private void initResponseMessages() {
    responses = newLinkedHashMap();
    responses.put(GET, asList(
            new ResponseMessageBuilder()
                    .code(OK.value())
                    .message(OK.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(NOT_FOUND.value())
                    .message(NOT_FOUND.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null).build()));

    responses.put(PUT, asList(
            new ResponseMessageBuilder()
                    .code(CREATED.value())
                    .message(CREATED.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(NOT_FOUND.value())
                    .message(NOT_FOUND.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null).build()));

    responses.put(POST, asList(
            new ResponseMessageBuilder()
                    .code(CREATED.value())
                    .message(CREATED.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(NOT_FOUND.value())
                    .message(NOT_FOUND.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null).build()));

    responses.put(DELETE, asList(
            new ResponseMessageBuilder()
                    .code(NO_CONTENT.value())
                    .message(NO_CONTENT.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null)
                    .build()));

    responses.put(PATCH, asList(
            new ResponseMessageBuilder()
                    .code(NO_CONTENT.value())
                    .message(NO_CONTENT.getReasonPhrase())
                    .responseModel(null).build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null)
                    .build()));

    responses.put(TRACE, asList(
            new ResponseMessageBuilder()
                    .code(NO_CONTENT.value())
                    .message(NO_CONTENT.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null)
                    .build()));

    responses.put(OPTIONS, asList(
            new ResponseMessageBuilder()
                    .code(NO_CONTENT.value())
                    .message(NO_CONTENT.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null)
                    .build()));
    responses.put(HEAD, asList(
            new ResponseMessageBuilder()
                    .code(NO_CONTENT.value())
                    .message(NO_CONTENT.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(FORBIDDEN.value())
                    .message(FORBIDDEN.getReasonPhrase())
                    .responseModel(null)
                    .build(),
            new ResponseMessageBuilder()
                    .code(UNAUTHORIZED.value())
                    .message(UNAUTHORIZED.getReasonPhrase())
                    .responseModel(null)
                    .build()));
  }


}
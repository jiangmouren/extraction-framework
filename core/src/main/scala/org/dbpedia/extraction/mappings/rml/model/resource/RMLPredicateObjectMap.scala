package org.dbpedia.extraction.mappings.rml.model.resource

import org.apache.jena.rdf.model.Resource
import org.dbpedia.extraction.mappings.rml.model.voc.Property
import org.dbpedia.extraction.ontology.RdfNamespace

/**
  * Represents a RML Predicate Object Map
  */
class RMLPredicateObjectMap(override val resource: Resource) extends RMLResource(resource) {

  lazy val predicatePropertyURI = getPredicate
  lazy val objectMap : RMLObjectMap = getObjectMap

  def addPredicate(uri: RMLUri) =
  {
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "predicate"), createProperty(uri.toString()))
  }

  def addPredicate(literal: RMLLiteral) =
  {
    resource.addLiteral(createProperty(RdfNamespace.RR.namespace + "predicate"), literal.toString())
  }

  def addObjectMap(uri: RMLUri) : RMLObjectMap =
  {
    val objectMap = factory.createRMLObjectMap(uri)
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "objectMap"), objectMap.resource)
    objectMap
  }

  def addObject(uri: RMLUri) : RMLPredicateObjectMap =
  {
    val toString = uri.toString
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "object"), model.createResource(toString))
    this
  }

  def addObject(literal: RMLLiteral) : RMLPredicateObjectMap =
  {
    resource.addLiteral(createProperty(RdfNamespace.RR.namespace + "object"), literal.toString())
    this
  }

  def addConditionalMap(uri: RMLUri) : RMLConditionalObjectMap =
  {
    val conditionalMap = factory.createRMLConditionalObjectMap(uri)
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "objectMap"), conditionalMap.resource)
    conditionalMap
  }

  def addFunctionTermMap(uri: RMLUri) : RMLFunctionTermMap =
  {
    val functionTermMap = factory.createRMLFunctionTermMap(uri)
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "objectMap"), functionTermMap.resource)
    functionTermMap
  }

  def addFunctionTermMap(functionTermMap: RMLFunctionTermMap) =
  {
    resource.addProperty(createProperty(RdfNamespace.RR.namespace + "objectMap"), functionTermMap.resource)
  }

  def addDCTermsType(literal: RMLLiteral) = {
    //resource.addLiteral(createProperty(RdfNamespace.DCTERMS.namespace + "type"), literal.toString())
  }

  def addDBFStartDate(predicateObjectMap: RMLPredicateObjectMap) =
  {
    resource.addProperty(createProperty(RdfNamespace.DBF.namespace + "startDate"), predicateObjectMap.resource)
  }

  def addDBFEndDate(predicateObjectMap: RMLPredicateObjectMap) =
  {
    resource.addProperty(createProperty(RdfNamespace.DBF.namespace + "endDate"), predicateObjectMap.resource)
  }

  def addPartner(predicateObjectMap: RMLPredicateObjectMap) =
  {
    resource.addProperty(createProperty(RdfNamespace.DBF.namespace + "partner"), predicateObjectMap.resource)
  }

  private def getPredicate : String = {
    val property = resource.listProperties(createProperty(Property.PREDICATE))
    val stmnt = property.nextStatement()
    stmnt.getObject.asResource().getURI
  }

  private def getObjectMap : RMLObjectMap = {
    val property = resource.listProperties(createProperty(Property.OBJECTMAP))
    if(property.hasNext) {
      val stmnt = property.nextStatement()
      val objectMapProperty = stmnt.getObject
      if (objectMapProperty.isResource) RMLObjectMap(objectMapProperty.asResource()) else null
    } else null
  }

  def hasParentTriplesMap : Boolean = {
    objectMap != null && objectMap.parentTriplesMap != null
  }

  def hasReferenceObjectMap = {
    if(objectMap != null) {
      objectMap.hasReference
    } else false
  }

}

object RMLPredicateObjectMap {

  def apply(resource : Resource) = {
    new RMLPredicateObjectMap(resource)
  }

}
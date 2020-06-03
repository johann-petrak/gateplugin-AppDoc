<#assign convertString = "at.ofai.gate.appdoc.functions.ConvertString"?new() >
<#assign isConvertible = "at.ofai.gate.appdoc.functions.IsConvertible"?new() >
<#assign japeDoc = "at.ofai.gate.appdoc.functions.JapeDoc"?new() >

<#function expandUrl url>
<#-- TODO: only replace if global setting requests it!  -->
<#local tmpurl=url?replace("$gatehome$",gatehome+"/")>
<#local tmpurl=tmpurl?replace("$relpath$",relpath+"/")>
<#return tmpurl>
</#function>

<#function absolutePath4Url url>
<#-- This override the definition in the marcos.ftl file  -->
<#local tmpurl=url?replace("$gatehome$",gatehome+"/")>
<#local tmpurl=tmpurl?replace("$relpath$",relpath+"/")>
<#return tmpurl>
</#function>

<#function urlify pathtourl>
<#local ret="file://"+pathtourl>
<#if pathtourl?starts_with("http:") || pathtourl?starts_with("file:")>
<#local ret=pathtourl>
</#if>
<#return ret>
</#function>

<#function getFeature field featuresNode>
<#local node=featuresNode["localMap/entry/string[text()='${field}']"]>
<#local text="">
<#if node[0]??>
<#local text=node?parent?children[3]>
</#if>
<#return text>
</#function>

<#function getCommentFormat>
<#local commentFormat = getFeature("%@commentFormat", doc["/gate.util.persistence.GateApplication/application/features"]) >
<#if commentFormat == "">
<#local commentFormat = "plain">
</#if>
<#return commentFormat>
</#function>

<#function getComment featuresNode>
<#local comment=getFeature("@comment",featuresNode)>
<#local commentFormat = getCommentFormat() > 
<#local text=convertString(comment,commentFormat,outFormat)>
<#return text>
</#function>

<#function getVersion featuresNode>
<#local version=getFeature("@version",featuresNode)>
<#return version>
</#function>

<#function getAuthor featuresNode>
<#local author=getFeature("@author",featuresNode)>
<#return author>
</#function>

<#function getHelpURL featuresNode>
<#local hu=getFeature("%@helpURL",featuresNode)>
<#return hu>
</#function>

<#function getCorpusName applicationRoot>
<#local corpus=applicationRoot["/gate.util.persistence.GateApplication/application/corpus"]>
<#local name="(no corpus)">
<#if corpus[0]??>
<#local name=corpus["resourceName"]>
</#if>
<#return name>
</#function>

<#function getCorpusType applicationRoot>
<#local corpus=applicationRoot["/gate.util.persistence.GateApplication/application/corpus"]>
<#local name="(no corpus)">
<#if corpus[0]??>
<#local name=corpus["resourceType"]>
</#if>
<#return name>
</#function>


<#function getXMLValue node resourcePath>
<#local resourceNode=node[resourcePath]>
<#local name=resourceNode["resourceName"]>
<#local text="(error: name not found)">
<#if name[0]??>
<#local text=name[0]>
<#else>
<@pp.warning message="Error: could not find a resource name for path "+resourcePath/>
</#if>
<#return text>
</#function>




<#macro url theUrlHolder>
<#local ret=theUrlHolder["urlString"]>
<#if ret[0]??>
<#local ret=expandUrl(ret)>
<#else>
<#local ret="">
</#if>
${ret}</#macro>

<#-- TODO: process more controller types and provide a fallback for new ones! -->
<#macro list_prs4controller applicationNode>
<#local class=applicationNode["@class"]>
<#local list=[]>
<#switch class>
<#case "gate.util.persistence.ConditionalSerialAnalyserControllerPersistence">
<#local list=applicationNode["strategiesList/localList/gate.util.persistence.AnalyserRunningStrategyPersistence/pr"]>
<#break>
<#case "gate.util.persistence.SerialAnalyserControllerPersistence">
<#local list=applicationNode["prList/localList"]?children>
<#break>
<#case "gate.util.persistence.ConditionalControllerPersistence">
<#local list=applicationNode["strategiesList/localList/gate.util.persistence.AnalyserRunningStrategyPersistence/pr"]>
<#break>
<#case "gate.groovy.ScriptableControllerPersistence">
<#-- <#local list=applicationNode["prList/localList/gate.util.persistence.LanguageAnalyserPersistence"]> -->
<#local list=applicationNode["prList/localList/*"]>
<#break>
<#case "gate.util.persistence.ControllerPersistence">
<#local list=applicationNode["prList/localList"]?children>
<#break>
<#default>
<#local list=[]>
</#switch>
<#list list as e>
<#local childname=e?node_name>
<#if childname != "@text">
<#nested e>
</#if>
</#list>
</#macro>

<#function hasParms parmNode>
<#local flag=false>
<#if (parmNode?size > 0) >
<#if (parmNode["localMap/entry"]?size > 0) >
<#local flag=true>
</#if>
</#if>
<#return flag>
</#function>

<#-- returns true if the node passed is for a pr inside a conditional
     controller.
     For such a PR the functions for getting the runmode, feature and value
     can be used
-->
<#function hasCondition prNode>
<#local flag=false>
<#local runmode=prNode?parent["runMode"]>
<#if runmode[0]??>
<#local flag=true>
</#if>
<#return flag>
</#function>

<#function getConditionRunMode prNode>
<#local runmode=prNode?parent["runMode"]>
<#if runmode[0]??>
<#local runmode=runmode["@@text"]>
<#else>
<#local runmode="">
</#if>
<#return runmode>
</#function>

<#function getConditionFeature prNode>
<#local feature=prNode?parent["featureName"]!"">
<#return feature>
</#function>

<#function getConditionValue prNode>
<#local value=prNode?parent["featureValue"]!"">
<#return value>
</#function>

<#macro list_parms parmNode>
<#if (parmNode?size > 0) >
<#list parmNode["localMap/entry"] as e>
<#local key=e?children[1]>
<#local val=e?children[3]>
<#nested key, val>
</#list>
</#if>
</#macro>


<#macro format_collection node>
<#local list=node["localList"]>
<#if (list?size > 0)>
<#-- TODO: this should recursively format the elements!! -->
${list["@@text"]}
</#if>
</#macro>

<#macro format_value valueNode>
<#local nodename=valueNode?node_name>
<#switch nodename>
<#case "null">
(null)
<#break>
<#case "boolean">
<#case "string">
${valueNode}
<#break>
<#case "gate.util.persistence.PersistenceManager-URLHolder">
${expandUrl(valueNode["urlString"])}
<#break>
<#case "gate.util.persistence.CollectionPersistence">
<@format_collection valueNode/>
<#break>
<#default>
${valueNode["@@text"]}
</#switch>
</#macro>

<#function getUrlFeature name node>
<#local featurenode=getFeature(name,node)>
<#local nodename=featurenode?node_name>
<#local ret="">
<#if nodename=="gate.util.persistence.PersistenceManager-URLHolder">
<#local ret=absolutePath4Url(featurenode["urlString"])>
</#if>
</#function>

<#function isJapePR prnode>
<#local rt=prnode["resourceType"]>
<#local ret=false>
<#switch rt>
<#case "gate.creole.Transducer">
<#case "at.ofai.gate.japebackrefs.JAPEBackRefs">
<#case "gate.jape.plus.Transducer">
<#local ret=true>
<#break>
<#default>
<#local ret=false>
</#switch>
<#return ret>
</#function>

<#function getJapeFile prnode>
<#local rt=prnode["resourceType"]>
<#local parmName="grammarURL">
<#if rt=="gate.jape.plus.Transducer">
<#local parmName="sourceURL">
</#if>
<#local fileURL=getUrlFeature(parmName,prnode["initParams"])>
<#if !fileURL??>
<#local fileURL="">
</#if>
<#return fileURL>
</#function>

<#-- return the japeDoc datastructure -->
<#function getJapeDoc prnode toFormat>
<#local japeFile=getJapeFile(prnode)>
<#local japeDoc=japeDoc(japeFile,getCommentFormat(),toFormat)>
<#return japeDoc>
</#function>

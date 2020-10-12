package com.ect.communication.model.demo.data

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
@JsonInclude(JsonInclude.Include.NON_NULL)
class WebAppModel {

    @JsonProperty('from')
    From from
    @JsonProperty('to')
    To to
    @JsonProperty('message')
    AppMessage message

}

class From {

    @JsonProperty('type')
    String type

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty('number')
    String number
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty('id')
    String id
}

class To {
    @JsonProperty('type')
    String type

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty('number')
    String number

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty('id')
    String id
}

@JsonInclude(JsonInclude.Include.NON_NULL)
class AppMessage {

    @JsonProperty('content')
    Content content


}
@JsonInclude(JsonInclude.Include.NON_NULL)
class Content{
    @JsonProperty('type')
    String type
    @JsonProperty('text')
    String text
}

package com.example.githubcatalog.data.response

import com.google.gson.annotations.SerializedName

data class RelationshipResponse(

	@field:SerializedName("RelationshipResponse")
	val relationshipResponse: List<ItemsItem>
)

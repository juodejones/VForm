package com.jones.vform.data

data class Form(val name: String ?= null, val fields: List<Field> ?= null, val creator: String? = null)
package com.codepolitan.musicapp.models

import com.google.firebase.database.PropertyName

data class User(
  @get:PropertyName("uid")
  @set:PropertyName("uid")
  var uid: String? = null,

  @get:PropertyName("full_name")
  @set:PropertyName("full_name")
  var fullName: String? = null,

  @get:PropertyName("email")
  @set:PropertyName("email")
  var email: String? = null
)
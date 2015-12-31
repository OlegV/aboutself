package controllers

import java.util.regex.Pattern

import models._
import play.api.db.slick._
import play.api.Play.current
import play.api.libs.json.Json
import play.api.libs.json.Json._

import play.api.mvc._
import play.api.libs.json._
import play.api.libs.functional.syntax._

trait Application extends Controller {

  this: UserComponent =>

  implicit val rds = (
    (__ \ 'email).read[String] and
      (__ \ 'password).read[String]
    ) tupled


  implicit val userReads = Json.reads[User]
  val EMAIL_PATTERN =
    "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

  val pattern =  Pattern.compile(EMAIL_PATTERN)

  def validate(email: String): Boolean = {
    val matcher = pattern.matcher(email)
    matcher.matches()
  }

  def index = DBAction {
    request =>

      implicit val session = request.dbSession
      val response = request.session.get("user") match {
        case Some(email) =>

          userModel.findUser(email) map {
            user =>
              views.html.userDetails(user)
          } getOrElse views.html.index()

        case None => views.html.index()
      }

      Ok(response)

  }

  def login = DBAction(parse.json) {
    request =>
      implicit val session = request.dbSession

      val json = request.body

      json.validate[(String, String)].map {
        case (email, password) =>

          userModel.checkUser(email, password) match {
            case Some(user) =>
              Ok(Json.obj("status" -> 0, "connected" -> true)).withSession("user" -> user.email)
            case None =>
              Ok(Json.obj("status" -> 2, "error" -> "User not found"))
          }
      }.recoverTotal {
        e =>
          Ok(Json.obj("status" -> 1, "error" -> "Incorrect json"))
      }
  }

  def userDetails = DBAction {
    request =>
      implicit val session = request.dbSession

      request.session.get("user") match {
        case Some(email) =>

          userModel.findUser(email) map {
            user =>
              Ok(views.html.userDetails(user))
          } getOrElse Redirect("/")

        case None => Redirect("/")
      }

  }

  def register = DBAction(parse.json) {
    request =>
      implicit val session = request.dbSession

      val json = request.body
      val result = json.validate[User].map {
        case user =>

          if(validate(user.email)) {
            if (userModel.findUser(user.email).isEmpty) {
              userModel.saveUser(user)
              Json.obj("status" -> 0)
            } else Json.obj("status" -> 2, "error" -> "User exist")
          } else Json.obj("status" -> 3, "error" -> "Email not valid")

      }.recoverTotal {
        e =>
          Json.obj("status" -> 1, "error" -> "Incorrect json")
      }

      Ok(result)
  }

  def logout = Action {
    request =>
      Redirect("/").withNewSession
  }

}

object Application extends Application with UserComponentImpl
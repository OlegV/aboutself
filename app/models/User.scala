package models

import java.security.MessageDigest

import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._


case class User(email: String, firstName: String, lastName: String, city: String, password: String)

class UserTable(tag: Tag) extends Table[User](tag, "users") {

  def email = column[String]("email", O.PrimaryKey)

  def firstName = column[String]("first_name")

  def lastName = column[String]("last_name")

  def city = column[String]("city")

  def password = column[String]("password")

  def * = (email, firstName, lastName, city, password) <>(User.tupled, User.unapply _)

}

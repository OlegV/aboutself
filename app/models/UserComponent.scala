package models

import java.security.MessageDigest

import play.api.db.slick._
import play.api.db.slick.Config.driver.simple._


/**
 * Created by root on 12/30/15.
 */
trait UserComponent {

  def userModel: UserModel

  trait UserModel {
    def checkUser(email: String, password: String)(implicit session: Session): Option[User]

    def findUser(email: String)(implicit session: Session): Option[User]

    def saveUser(user: User)(implicit session: Session): Unit
  }

}

trait UserComponentImpl extends UserComponent {

  lazy val userModel = new UserModelImpl

  class UserModelImpl extends UserModel {

    val users = TableQuery[UserTable]

    override def checkUser(email: String, password: String)(implicit session: Session): Option[User] =
      users.filter(user => user.email === email && user.password === (md5(password) + md5(email))).list.headOption

    override def findUser(email: String)(implicit session: Session): Option[User] = users.filter(_.email === email).list.headOption

    override def saveUser(user: User)(implicit session: Session): Unit = users += user

    def md5(text: String) : String = MessageDigest.getInstance("MD5").digest(text.getBytes).map(0xFF & _).map { "%02x".format(_) }.foldLeft(""){_ + _}
  }

}
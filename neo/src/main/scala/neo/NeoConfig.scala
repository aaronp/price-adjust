package domain.dao.neo

import com.typesafe.config.Config
import org.anormcypher.Neo4jREST

case class NeoConfig(host: String, port: Int, path: String, username: String, password: String, cypherEndpoint: String, https: Boolean) {
  def newRestClient: Neo4jREST = {
    Neo4jREST(host = host,
      port = port,
      path = path,
      username = username,
      password = password,
      cypherEndpoint = cypherEndpoint,
      https = https)
  }
}

object NeoConfig {

  def apply(conf: Config): NeoConfig = {

    new NeoConfig(host = conf.getString("host"),
      port = conf.getInt("port"),
      path = conf.getString("path"),
      username = conf.getString("username"),
      password = conf.getString("password"),
      cypherEndpoint = conf.getString("cypherEndpoint"),
      https = conf.getBoolean("https"))
  }
}

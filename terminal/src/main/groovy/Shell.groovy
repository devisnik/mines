import org.codehaus.groovy.tools.shell.IO
import org.codehaus.groovy.tools.shell.Main

class Shell extends Main {

  private Shell(IO io) {
    super(io)
  }

  private start() {
    String pwd = new File('.').absolutePath
    println """Welcome to the dark side! (pwd = '$pwd')"""

    def startScript = '''\
          := interpreterMode
          := verbosity QUIET '''.stripIndent()
    startGroovysh(startScript, [])
  }

  static main(def args) {
//    setTerminalType('unix' , true /* suppress colors as we get weird char codes in terminal on autocompletion */)
    setSystemProperty 'groovysh.prompt=prompt'
    new Shell(new IO()).start()
  }
}

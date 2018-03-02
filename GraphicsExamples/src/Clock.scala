import scala.swing._
import java.awt.{Color, BasicStroke, Graphics2D}
import java.awt.RenderingHints
import java.awt.event.ActionListener


object StaticGraphics extends SimpleSwingApplication {
  
  def top = new MainFrame {
    title     = "Kello"
    resizable = false
    
    val width      = 200
    val height     = 200
    val fullHeight = 210
    
    // Komponentti ilmoittaa näin mini, maksimi ja toivekokonsa, joita Layout Manager mahdollisesti noudattaa
    // sijoitellessaan komponentteja ruudulle
    minimumSize   = new Dimension(width,fullHeight)
    preferredSize = new Dimension(width,fullHeight)
    maximumSize   = new Dimension(width,fullHeight)

    /* Ohut ja paksu viiva */
    val secondStroke = new BasicStroke(1)
    val minuteStroke = new BasicStroke(2)
    val hourStroke   = new BasicStroke(4)
    
    // kellon keskipisteen koordinaatit
    val cx = width / 2 
    val cy = width / 2
    
    // Kirjainten korkeuden vuoksi niiden keskipistettä on laskettu hieman
    val textCy = cy + 10 
    val hourMarkRadius   = 0.70 * width / 2 
    val textPosRadius    = 0.86 * width / 2

    // Tunnit ja minuutit muutetaan kulmiksi radiaaneissa
    def hourToAngle(h: Double)   = (h * 30.0 - 90) / 360 * 2 * math.Pi
    def minuteToAngle(h: Double) = (h * 6.0  - 90) / 360 * 2 * math.Pi

    // keskipiste, kulma ja etäisyys -> x, y
    def cartesianX(xCenter: Int, angle: Double, length: Double) =
      (xCenter + math.cos(angle) * length).toInt

    def cartesianY(yCenter: Int, angle: Double, length: Double) =
      (yCenter + math.sin(angle) * length).toInt


    def drawClock( g: Graphics2D ) = {
      /** piirretään kellotaulu */
      for (hour <- 1 to 12) {
        val angle = hourToAngle(hour)
      
        g.fillOval( cartesianX(cx, angle, hourMarkRadius) - 5, cartesianY(cy, angle, hourMarkRadius) - 5, 10, 10)

        g.drawString(hour.toString(), cartesianX(cx, angle, textPosRadius) - 5, cartesianY(textCy, angle, textPosRadius) - 5)
      }
        
      /** Haetaan aika ja muunnnetaan se asteiksi radiaaneina */
      import java.util.Calendar
      val hours = Calendar.getInstance.get(Calendar.HOUR)
      val mins  = Calendar.getInstance.get(Calendar.MINUTE)
      val secs  = Calendar.getInstance.get(Calendar.SECOND)
      
      val hourHand   = hourToAngle(hours + mins / 60.0)
      val minuteHand = minuteToAngle(mins)
      val secondHand = minuteToAngle(secs)
      
      /** piirretään viisarit eri paksuisina ja pituisina viivoina */
      g.setStroke(hourStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, hourHand, hourMarkRadius * 0.7),
          cartesianY(cy, hourHand, hourMarkRadius * 0.7))

      g.setStroke(minuteStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, minuteHand, hourMarkRadius * 0.9),
          cartesianY(cy, minuteHand, hourMarkRadius * 0.9))

      g.setStroke(secondStroke)
      g.drawLine( cx, cy,
          cartesianX(cx, secondHand, hourMarkRadius * 0.95),
          cartesianY(cy, secondHand, hourMarkRadius * 0.95))

    }
      
      
    val clock = new Panel {

      // Tämän metodin korvaaminen mahdollistaa oman grafiikan piirtämisen
      // Se saa parametrinaan Graphics2D-olion, jonka kautta komponenttiin voi tehdä
      // piirto-operaatioita, vaihtaa piirtovärejä, koordinaatistoa, viivan paksuutta jne.
      
      override def paintComponent(g: Graphics2D) = {

        g.setColor(new Color(80, 180, 235))
        g.fillRect(0, 0, width, fullHeight)

        // Siloiteltu grafiikka ns. antialiasointi
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)          

        g.translate(-1, -1) // Varjo piirretään vähän sivuun
        g.setColor(Color.black)
        drawClock(g)
        g.translate(1, 1)   // ja koordinaatisto laitetaan takaisin paikalleen   

        // Varsinainen teksti piirretään varjon päälle
        g.setColor(Color.white)
        drawClock(g)
        
      }    
      
    }  
    
    // SimpleSwingApplication sisältää vain "kellopaneelin"
    contents = clock

    // Tämä tapahtumankuuntelija ja swing timer mahdollistavat määräajoin toistuvan
    // toiminnan tapahtumankuuntelusäikeessä. Kello on riittävän kevyt piirtää
    // säikeessä ilman lisäpuskureita tai lisäsäikeitä
    
    val listener = new ActionListener(){
      def actionPerformed(e : java.awt.event.ActionEvent) = clock.repaint()  
    }

    // Timer lähettää ActionListenerille ActionEventin n. 1000ms (1 sekunti) välein
    // Tämä event suoritetaan event listener -säikeessä
    val timer = new javax.swing.Timer(1000, listener)
    timer.start()
    
    
  }
 
}
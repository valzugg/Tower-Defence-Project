import scala.swing._
import java.awt.{ Point, Rectangle }
import scala.io.Source
import javax.imageio.ImageIO
import java.io.File
import java.awt.image.BufferedImage
import scala.swing.event.MouseMoved

/**
 *  Tässä esimerkissä käsitellään mm seuraavia asioita:
 * 
 *  + Kuvan lataaminen tiedostosta
 *  + Kuvan tallentaminen tiedostoon
 *  + Ruudulle piirtäminen
 *  + Kuvaan piirtäminen
 *  + csv-tiedoston lukeminen
 *  + Kaupungin kartan piirtäminen spriteistä
 *  + Hiiritapahtumien kuuntelu
 *  + karttaruutujen valinta hiirellä.
 *  
 *  Aja ohjelma ja kokeile viedä hiiri kartan päälle.
 * 
 *  Karttaspritet : Kenney, http://opengameart.com
 *  csv-tiedosto muokattu opengameart-projektin xml-tiedostosta
 *  
 */

object CityTiles extends SimpleSwingApplication {

  // Yksittäistä spriteä kuvaava luokka, joka säilää kuva, mittasuhteet, id:n sekä mahdollisen lisädatan (string-muotoisia "lippuja")
  case class Sprite(id: String, image: BufferedImage, width: Int, height: Int, data: Set[String])

  def getSprites(dataFile: String, imageFile: String) = {
    // Luetaan spritejen idt ja paikat kuvassa
    val lines = Source.fromFile(dataFile).getLines()

    // Luetaan kuvatiedosto 
    val spriteSheet = ImageIO.read(new File("cityTiles_sheet.png"))

    // Käydään spritet läpi ja irroitetaan ne isosta kuvasta.
    // Lopputulos palautetaan funktiosta listana
    for {
      line <- lines.toList
      items = line.split(",")
      id = items.head
      bounds = items.tail.take(4).map(_.toInt)
      data = items.drop(5).toSet
      image = spriteSheet.getSubimage(bounds(0), bounds(1), bounds(2), bounds(3))
    } yield Sprite(id, image, bounds(2), bounds(3), data)
  }

  // Tällä metodilla voidaan merkitä alkuperäiseen sprite sheet:iin spritejen numerot
  // Huom: Tätä metodia ei kutsuta projektissa - voit kokeilla sitä itse
 
  def markSpriteNumbers(dataFile: String, imageFile: String, outFileName: String) = {
    // Luetaan spritejen idt ja paikat kuvassa.
    val lines = Source.fromFile(dataFile).getLines()

    // Luetaan kuvatiedosto
    val spriteSheet = ImageIO.read(new File("cityTiles_sheet.png"))

    // Haetaan graphics-olio, jolla kuvaan voi piirtää
    val sheetGraphics = spriteSheet.getGraphics()

    // Käydään spritet läpi ja merkitään niiden numerot isoon kuvaan.
    for (
      line <- lines
    ) {
      val items = line.split(",")
      val id = items.head
      val bounds = items.tail.take(4).map(_.toInt)

      sheetGraphics.setColor(java.awt.Color.BLACK)
      sheetGraphics.drawString(id, bounds(0), bounds(2))
    }

    ImageIO.write(spriteSheet, "png", new File(outFileName))
  }

  def top = new MainFrame {

    title = "City map tests"

    val width = 800
    val height = 560

    /**
     * (swing)-Komponentti ilmoittaa näin minimi-, maksimi ja toivekokonsa, joita Layout Manager mahdollisesti noudattaa
     * sijoitellessaan komponentteja ruudulle.
     */
    minimumSize = new Dimension(width, height)
    preferredSize = new Dimension(width, height)
    maximumSize = new Dimension(width, height)

    // Ladataan Spritet tiedostosta

    val sprites = getSprites("sprites.csv", "cityTiles_sheet.png")

    val selectionMask = ImageIO.read(new File("selection.png"))
    /**
     * city- component hoitaa kartan piirtelyn
     */

    val city = new Component {

      // Kartta, jonka numerot ovat spritejen numeroita. Tyypillisesti tämä
      // laitettaisiin ulkoiseen tiedostoon

      val kartta = List(
        List(124, 103, 126, 76, 4),
        List(44, 29, 30, 75, 4),
        List(56, 40, 81, 61, 16),
        List(3, 43, 56, 52, 53),
        List(122, 57, 96, 100, 80))

      // Myähemmin tässä luokassa on koodi, jolla spritejä voi valita hiirellä
      // Tähän muuttujaan tallennetaan valitun sprite paikka kartalla
      var selectedSprite: Option[(Int, Int)] = None

      val leftPadding = 80
      val topPadding = 300
      val tileWidth = 132
      val halfWidth = 66
      val fullHeight = 66
      val halfHeight = 33

      // Lasketaan koordinaatit ruudulla
      val positions = Vector.tabulate(5, 5) { (x: Int, y: Int) =>

        val sprite = sprites(kartta(x)(y))

        val xc = leftPadding + x * halfWidth + y * fullHeight
        val yc = topPadding - x * halfHeight - sprite.height + y * halfHeight

        new Point(xc, yc)
      }

      /**
       * Piirtofunktio  - Huomaa että suurin osa laskuista on tehty tämän ulkopuolella valmiiksi
       */
      override def paintComponent(g: Graphics2D) = {

        for {
          y <- 0 until 5
          x <- 4 to 0 by -1
        } {
          val place = positions(x)(y)
          val sprite = sprites(kartta(x)(y))

          g.drawImage(sprite.image, place.x, place.y, null)

          // Jos selectedSprite ei ole tyhjä, katsotaan onko se valittu yksilä
          selectedSprite.foreach {
            mapCoords =>
              if (mapCoords._1 == x && mapCoords._2 == y) {
                g.drawImage(selectionMask, place.x, place.y + sprite.height - selectionMask.getHeight, null)
              }
          }
        }
      }

      // Jotta spritejä voi valita lasketaan bounding box:it, eli laatikot, joiden
      // sisällä spritet ovat
      val boundingBoxes = (
        for {
          x <- 0 until 5
          y <- 4 to 0 by -1
          pos = positions(x)(y)
          sprite = sprites(kartta(x)(y))
        } yield new Rectangle(pos.x, pos.y, sprite.width, sprite.height) -> (x, y))

      // Valintaa varten kuunnellaan hiirtä
      listenTo(mouse.moves)

      // ja kerrotaan lisäksi kuinka asioihin tulee reagoida
      reactions += {
        case MouseMoved(c, point, mods) => {
          
          // Otetaan talteen mahdollisesti valitun spriten sijainti kartalla
          selectedSprite = boundingBoxes.find(box => box._1.contains(point)).map(_._2)

          // komponentti pitää piirtää uudelleen, jotta valinta saadaan näkyviin
          repaint()
        }
      }

    }
    contents = city
  }

}
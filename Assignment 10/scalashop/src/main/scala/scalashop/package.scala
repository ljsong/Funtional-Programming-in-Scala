
import common._

package object scalashop {

  /** The value of every pixel is represented as a 32 bit integer. */
  type RGBA = Int

  /** Returns the red component. */
  def red(c: RGBA): Int = (0xff000000 & c) >>> 24

  /** Returns the green component. */
  def green(c: RGBA): Int = (0x00ff0000 & c) >>> 16

  /** Returns the blue component. */
  def blue(c: RGBA): Int = (0x0000ff00 & c) >>> 8

  /** Returns the alpha component. */
  def alpha(c: RGBA): Int = (0x000000ff & c) >>> 0

  /** Used to create an RGBA value from separate components. */
  def rgba(r: Int, g: Int, b: Int, a: Int): RGBA = {
    (r << 24) | (g << 16) | (b << 8) | (a << 0)
  }

  /** Restricts the integer into the specified range. */
  def clamp(v: Int, min: Int, max: Int): Int = {
    if (v < min) min
    else if (v > max) max
    else v
  }

  /** Image is a two-dimensional matrix of pixel values. */
  class Img(val width: Int, val height: Int, private val data: Array[RGBA]) {
    def this(w: Int, h: Int) = this(w, h, new Array(w * h))
    def apply(x: Int, y: Int): RGBA = data(y * width + x)
    def update(x: Int, y: Int, c: RGBA): Unit = data(y * width + x) = c
  }

  /** Computes the blurred RGBA value of a single pixel of the input image. */
  def boxBlurKernel(src: Img, x: Int, y: Int, radius: Int): RGBA = {
    // TODO implement using while loops
    var ix, jx = -radius
    var count = 0
    var redSum, greenSum, blueSum, alphaSum = 0

    while (ix <= radius) {
      jx = -radius
      while (jx <= radius) {
        if (x + ix >= 0 && x + ix < src.width && y + jx >= 0 && y + jx < src.height) {
          redSum = redSum + red(src(x + ix, y + jx))
          greenSum = greenSum + green(src(x + ix, y + jx))
          blueSum = blueSum + blue(src(x + ix, y + jx))
          alphaSum = alphaSum + alpha(src(x + ix, y + jx))
          count = count + 1
        }
        jx = jx + 1
      }
      ix = ix + 1
    }

    rgba(redSum / count, greenSum / count, blueSum / count, alphaSum / count)

    /*
    val neighbors = for {
      ix <- -radius to radius
      jx <- -radius to radius
      if (x + ix >= 0 && x + ix < src.width && y + jx >= 0 && y + jx < src.height)
    } yield (src(x + ix, y + jx))

    rgba((neighbors map (red(_))).sum / neighbors.length,
      (neighbors map (green(_))).sum / neighbors.length,
      (neighbors map (blue(_))).sum / neighbors.length,
      (neighbors map (alpha(_))).sum /neighbors.length) */
  }

}

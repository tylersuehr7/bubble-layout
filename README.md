# Bubble Layout
An Android library that contains a bubble layout... similar to the chat bubbles on Facebook Messenger.

### This project contains these views:
 * **`CircleImageView`**: Implementation of `ImageView` that crops the image into a circle and draws a border around it.
 * **`CircleCountView`**: Draws a circle and draws text in the center that adjusts its size dynamically to fit.
 * **`BubbleLayou`t**: Shows `CircleImageView` and a `CircleCountView` with the extra count when a peek is reached.
 
 ### Other objects:
 * **`FontCache`**: Utility class to cache any `Typeface` for the custom fonts in the assets folder.
 * **`ObjectPool`**: Basic Object Pool design pattern used by `BubbleLayout` to create the `CircleImageView`.

### Visuals
![Screenshot](https://github.com/tylersuehr7/bubble-layout/blob/master/img_screenshot_1.png "SC1")

![Screenshot](https://github.com/tylersuehr7/bubble-layout/blob/master/img_screenshot_2.png "SC2")

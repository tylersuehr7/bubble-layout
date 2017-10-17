# Bubble Layout
*Documentation and library updates still in progress*

An Android library that contains a bubble layout... similar to the chat bubbles on Facebook Messenger.

<img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_1.png" width="200"> <img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_2.png" width="200">

### This project contains these views:
 * **`CircleImageView`**: Implementation of `ImageView` that crops the image into a circle and draws a border around it.
 * **`CircleCountView`**: Draws a circle and draws text in the center that adjusts its size dynamically to fit.
 * **`BubbleLayout`**: Shows `CircleImageView` and a `CircleCountView` with the extra count when a peek is reached.
 
 ### Other objects:
 * **`FontCache`**: Utility class to cache any `Typeface` for the custom fonts in the assets folder.
 * **`ObjectPool`**: Basic Object Pool design pattern used by `BubbleLayout` to create the `CircleImageView`.

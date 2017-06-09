# Bubble Layout
An Android library that contains a bubble layout... similar to the chat bubbles on Facebook Messenger.

### This project contains these views:
 * **CircleImageView**: Implementation of ImageView that crops the image into a circle and draws a border around it.
 * **CircleCountView**: Drawas a circle and draws text in the center that adjusts its size dynamically to fit.
 * **BubbleLayout**: Shows CircleImageViews and a CircleCountView with the extra count when a peek is reached.
 
 ### Other objects:
 * **FontCache**: Utility class to cache Typefaces for the custom fonts in the assets folder.
 * **ObjectPool**: Basic Object Pool design pattern used by BubbleLayout to create CircleImageViews.

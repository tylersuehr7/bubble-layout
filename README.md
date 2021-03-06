# Bubble Layout
A customizable Android ViewGroup that displays avatar bubbles... similar to the chat bubbles on Facebook Messenger.

<img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_1.png" width="200"> <img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_2.png" width="200">

How to use it...

In your project level build.gradle :
```java
allprojects {
    repositories {
        ...
        maven { url "https://jitpack.io" }
    }
} 
```

In your app level build.gradle:
```java
dependencies {
    compile 'com.github.tylersuehr7:bubble-layout:1.0'
}  
```

## Using the Bubble Layout
The intended purpose of this library is to afford a ViewGroup that displays avatar bubbles that can have a limit on how many can be shown on the screen at once before displaying an extra count.

A typical use-case for this type of ViewGroup could be something like displaying the users attending an event, but you only want to show the images of the first few users and still emphasize how many people are attending the event.

To use the intended functionality, you'll need to use the `BubbleLayout`.

### Using in an XML layout
`BubbleLayout` can be used inside any other ViewGroup and supports all width and height attributes. Supported views can be dynamically added to the `BubbleLayout` or statically added using the XML layout file. Simple examples of both are shown below.

Example 1:
```xml
<com.tylersuehr.bubbles.BubbleLayout
    android:id="@+id/bubbles"
    android:layout_width="wrap_content"
    android:layout_height="64dp"
    android:layout_marginTop="16dp"
    android:textColor="@color/colorPrimary"
    app:bubbleSize="64dp"
    app:bubblePeek="3"/>
```

Example 2:
```xml
<com.tylersuehr.bubbles.BubbleLayout
    android:id="@+id/bubbles"
    android:layout_width="wrap_content"
    android:layout_height="64dp"
    android:layout_marginTop="16dp"
    android:textColor="@color/colorPrimary"
    app:bubbleSize="64dp"
    app:bubblePeek="3">
    <com.tylersuehr.bubbles.CircleImageView ... />
    <com.tylersuehr.bubbles.CircleCountView ... />
</com.tylersuehr.bubbles.BubbleLayout>
```

<attr name="android:textColor"/>
        <attr name="borderColor"/>
        <attr name="borderWidth"/>
        <attr name="bubbleSize"/>
        <attr name="bubblePeek"/>
        <attr name="bubbleOffset"/>
        <attr name="bubbleMargin"/>
        <attr name="useBubbleOffset"/>

Here is a table of all the XML attributes available for this view:

Attribute | Type | Summary
--- | :---: | ---
`android:textColor` | `color` | Sets the text color used by the extra count view.
`app:borderColor` | `color` | The color of each bubbles' border.
`app:borderWidth` | `dimension` | The size of each bubbles' border.
`app:bubbleSize` | `dimension` | The size of each bubble.
`app:bubblePeek` | `int` | How many avatar bubbles should show before displaying the extra count.
`app:bubbleOffset` | `int` | The offset overlap of each bubble on each other.
`app:bubbleMargin` | `dimension` | The spacing in between each bubble.
`app:useBubbleOffset` | `boolean` | True if bubbles should overlap and use offset.

### Using in Java code
`BubbleLayout` can be programmatically added into any ViewGroup. Simple usage in an Activity is shown here:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    BubbleLayout bubbles = new BubbleLayout(this);
    // Set any properties for bubble layout
    
    setContentView(bubbles);
}
```

Here is a table of all the accessible attributes available for this view:

Method | Summary
--- | ---
`setUseBubbleOffset(boolean)` | True if bubbles should overlap each other and use offsets rather than margins.
`setBubbleSize(int)` | Sets the size of each bubble.
`setBubblePeek(int)` | Sets how many avatar bubbles should be shown before showing the extra count.
`setBubbleOffset(int)` | Sets the overlapping offset of each bubble (only used when useOffset is true).
`setBubbleMargin(int)` | Sets the spacing between each bubble (only used when useOffset is false).
`setBubbleBorderWidth(int)` | Sets the border width of each bubble.
`setBubbleBorderColor(int)` | Sets the border color of each bubble.
`setBubbleBorderColorResource(int)` | Sets the border color of each bubble using color resource.
`setBubbleTextColor(int)` | Sets the text color of each bubble (used by count bubble).
`setBubbleTextColorResource(int)` | Sets the text color of each bubble using color resource (used by count bubble).

## Using Bubbles
Bubbles are the core feature of this library, but what are they? Bubbles are a circular shape that either shows an avatar image or displays an extra count (like, "+3").

`BubbleLayout` manages all of the bubbles, and their views, for you! It affords you the ability to add, remove, or clear bubbles in it, and will handle displaying the extra count bubble when the set peek is reached.

### Adding a bubble
Adding a bubble can be done by using any of the overloads of `addBubble(...)` in `BubbleLayout`. Simple examples are shown here:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    BubbleLayout bubbles = new BubbleLayout(this);
    setContentView(bubbles);
    
    // Example of adding a bubble using a drawable
    bubbles.addBubble(anyCoolDrawable);
    
    // Example of adding another bubble using a bitmap
    bubbles.addBubble(anyCoolBitmap);
    
    // Example of adding another bubble using a drawable resource
    bubbles.addBubble(R.drawable.cool_image);
}
```

### Removing all bubbles
Removing all bubbles can be done by using the `clearBubbles()` method in `BubbleLayout`. A simple example is shown here:
```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    
    BubbleLayout bubbles = new BubbleLayout(this);
    setContentView(bubbles);
    
    addCoolBubblesToBubbleLayout();
    
    // Example of removing all shown bubbles
    bubbles.clearBubbles();
}
```

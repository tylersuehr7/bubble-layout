# Bubble Layout
A customizable Android ViewGroup that displays avatar bubbles... similar to the chat bubbles on Facebook Messenger.

<img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_1.png" width="200"> <img src="https://github.com/tylersuehr7/bubble-layout/blob/master/docs/screen_bubbles_2.png" width="200">

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

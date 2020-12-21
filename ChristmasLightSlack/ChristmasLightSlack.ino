#include <PololuLedStrip.h>

PololuLedStrip<12> ledStrip;

#define LED_COUNT 100

rgb_color colors[LED_COUNT];

uint16_t ledIndex = 0;
String inData;

void setup() {
  Serial.begin(9600);
}

void loop() {
  while (Serial.available() > 0) {
    inData = Serial.readStringUntil('\n');
    Serial.print(inData);

    if (inData.length() == 100) {
      for (ledIndex = 0; ledIndex < 100; ledIndex++) {
        rgb_color color = getColor(inData.charAt(ledIndex));
        colors[ledIndex] = color;
      }
      ledStrip.write(colors, LED_COUNT);
    }
  }
  inData = "";
  delay(20);
}
// For reasons unknown the red and green channels appear to be back to front on the LEDs I have, so are switched here.
rgb_color getColor(char aChar) {
  rgb_color color;
  switch (aChar) {
    case '0':
      color.red = 0;
      color.green = 255;
      color.blue = 0;
      break;
    case '1':
      color.red = 255;
      color.green = 0;
      color.blue = 0;
      break;
    case '2':
      color.red = 0;
      color.green = 0;
      color.blue = 255;
      break;
    case '3':
      color.red = 255;
      color.green = 255;
      color.blue = 255;
      break;
    case '4':
      color.red = 255;
      color.green = 255;
      color.blue = 0;
      break;
    case '5':
      color.red = 0;
      color.green = 255;
      color.blue = 255;
      break;
    case '6':
      color.red = 200;
      color.green = 255;
      color.blue = 0;
      break;
    case '7':
      color.red = 175;
      color.green = 255;
      color.blue = 175;
      break;
    case '8':
      color.red = 255;
      color.green = 0;
      color.blue = 255;
      break;
    case '9':
      color.red = 0;
      color.green = 0;
      color.blue = 0;
      break;
  }
  return color;
}

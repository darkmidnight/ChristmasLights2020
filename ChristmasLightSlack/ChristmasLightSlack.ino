#include <PololuLedStrip.h>

PololuLedStrip<12> ledStrip;

#define LED_COUNT 100

rgb_color colors[LED_COUNT];

uint16_t ledIndex = 0;

byte inBytes[4];

void setup() {
  Serial.begin(9600);
  Serial.setTimeout(1000 * 60 * 5);
}

void loop() {
  int retVal = Serial.readBytes(inBytes, 4);
  if (retVal == 4) {
    if (inBytes[0] == 0) {
      for (int i = 0; i < 100; i++) {  //All
        rgb_color color;
        color.red = inBytes[1];
        color.green = inBytes[2];
        color.blue = inBytes[3];
        colors[i] = color;
      }
    } else if (inBytes[0] == 101) {  //Top
      for (int i = 50; i < 100; i++) {
        rgb_color color;
        color.red = inBytes[1];
        color.green = inBytes[2];
        color.blue = inBytes[3];
        colors[i] = color;
      }
    } else if (inBytes[0] == 102) {  //Bottom
      for (int i = 0; i < 50; i++) {
        rgb_color color;
        color.red = inBytes[1];
        color.green = inBytes[2];
        color.blue = inBytes[3];
        colors[i] = color;
      }
    } else if (inBytes[0] == 103) {  //Odd
      for (int i = 0; i < 100; i = i + 2) {
        rgb_color color;
        color.red = inBytes[1];
        color.green = inBytes[2];
        color.blue = inBytes[3];
        colors[i] = color;
      }
    } else if (inBytes[0] == 104) {  //Even
      for (int i = 1; i < 100; i = i + 2) {
        rgb_color color;
        color.red = inBytes[1];
        color.green = inBytes[2];
        color.blue = inBytes[3];
        colors[i] = color;
      }
    } else if (inBytes[0] > 0 && inBytes[0] < 101) { //Specific
      rgb_color color;
      color.red = inBytes[1];
      color.green = inBytes[2];
      color.blue = inBytes[3];
      colors[inBytes[0]] = color;
    }
  }
  ledStrip.write(colors, LED_COUNT);
  Serial.println("READY");
}
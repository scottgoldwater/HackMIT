
#include "pebble_os.h"
#include "pebble_app.h"
#include "pebble_fonts.h"
#include "resource_ids.auto.h"
#include <stdint.h>
#include <string.h>

#define BITMAP_BUFFER_BYTES 1024


#define MY_UUID { 0x56, 0x62, 0x88, 0xF3, 0xB4, 0x6E, 0x4B, 0xED, 0xA1, 0x60, 0x92, 0x57, 0xF5, 0x25, 0xCF, 0x7D }
PBL_APP_INFO(MY_UUID,
             "Pebble Go", "Concordia University",
             1, 0, /* App version */
             RESOURCE_ID_logo_icon,
             APP_INFO_STANDARD_APP);

static struct GoData {
  Window window;
  TextLayer direction_layer;
  TextLayer time_layer;

  BitmapLayer icon_layer;
  uint32_t current_icon;
  HeapBitmap icon_bitmap;
  AppSync sync;
  uint8_t sync_buffer[1024];
} s_data;

static bool callbacks_registered;

enum {
  GO_ICON_KEY = 0x0,         // TUPLE_INT
  GO_DIRECTION_KEY = 0x3,  // TUPLE_CSTRING
  GO_TIME_KEY = 0x4,
  CMD_KEY = 0x5, // TUPLE_INTEGER
  CMD_UP = 0x06,
  CMD_DOWN = 0x07,
};

static uint32_t GO_ICONS[] = {
  RESOURCE_ID_go_left,
  RESOURCE_ID_go_right,
  RESOURCE_ID_depart_icon,
  RESOURCE_ID_final_icon,
  RESOURCE_ID_load_icon
};

static void load_bitmap(uint32_t resource_id) {
  // If that resource is already the current icon, we don't need to reload it
  if (s_data.current_icon == resource_id) {
    return;
  }
  // Only deinit the current bitmap if a bitmap was previously loaded
  if (s_data.current_icon != 0) {
    heap_bitmap_deinit(&s_data.icon_bitmap);
  }
  // Keep track of what the current icon is
  s_data.current_icon = resource_id;
  // Load the new icon
  heap_bitmap_init(&s_data.icon_bitmap, resource_id);
}

static void app_send_failed(DictionaryIterator* failed, AppMessageResult reason, void* context) {
  // TODO: error handling
}

static void app_received_msg(DictionaryIterator* received, void* context) {
  vibes_short_pulse();
}

static void send_cmd(uint8_t cmd) {
  Tuplet value = TupletInteger(CMD_KEY, cmd);
  
  DictionaryIterator *iter;
  app_message_out_get(&iter);
  
  if (iter == NULL)
    return;
  
  dict_write_tuplet(iter, &value);
  dict_write_end(iter);
  
  app_message_out_send();
  app_message_out_release();
}

void up_single_click_handler(ClickRecognizerRef recognizer, Window *window) {
  load_bitmap(GO_ICONS[(uint8_t) 4]);
  bitmap_layer_set_bitmap(&s_data.icon_layer, &s_data.icon_bitmap.bmp);
  text_layer_set_text(&s_data.direction_layer, "...");	
  send_cmd(CMD_UP);
	
}

void down_single_click_handler(ClickRecognizerRef recognizer, Window *window) {
  load_bitmap(GO_ICONS[(uint8_t) 4]);
  bitmap_layer_set_bitmap(&s_data.icon_layer, &s_data.icon_bitmap.bmp);

  text_layer_set_text(&s_data.direction_layer, "...");	
  send_cmd(CMD_DOWN);
}

void click_config_provider(ClickConfig **config, Window *window) {
  
  config[BUTTON_ID_UP]->click.handler = (ClickHandler) up_single_click_handler;
  config[BUTTON_ID_UP]->click.repeat_interval_ms = 100;
  
  config[BUTTON_ID_DOWN]->click.handler = (ClickHandler) down_single_click_handler;
  config[BUTTON_ID_DOWN]->click.repeat_interval_ms = 100;
}


// TODO: Error handling
static void sync_error_callback(DictionaryResult dict_error, AppMessageResult app_message_error, void *context) {
}

static void sync_tuple_changed_callback(const uint32_t key, const Tuple* new_tuple, const Tuple* old_tuple, void* context) {
  vibes_short_pulse();
  switch (key) {
  case GO_ICON_KEY:
    load_bitmap(GO_ICONS[new_tuple->value->uint8]);
    bitmap_layer_set_bitmap(&s_data.icon_layer, &s_data.icon_bitmap.bmp);
    break;
  case GO_DIRECTION_KEY:
    // App Sync keeps the new_tuple around, so we may use it directly
    text_layer_set_text(&s_data.direction_layer, new_tuple->value->cstring);
    break;
  case GO_TIME_KEY:
    // App Sync keeps the new_tuple around, so we may use it directly
    text_layer_set_text(&s_data.time_layer, new_tuple->value->cstring);
    break;
  default:
    return;
  }
}

static void go_app_init(AppContextRef c) {

  s_data.current_icon = 0;

  resource_init_current_app(&GO_APP_RESOURCES);

  Window* window = &s_data.window;
  window_stack_push(window, true);
  window_init(window, "Pebble Go");
  window_set_background_color(window, GColorBlack);
  window_set_fullscreen(window, true);

  GRect icon_rect = (GRect) {(GPoint) {32, 10}, (GSize) { 80, 80 }};
  bitmap_layer_init(&s_data.icon_layer, icon_rect);
  layer_add_child(&window->layer, &s_data.icon_layer.layer);

  text_layer_init(&s_data.direction_layer, GRect(0, 90, 144, 70));
  text_layer_set_text_color(&s_data.direction_layer, GColorWhite);
  text_layer_set_background_color(&s_data.direction_layer, GColorClear);
  text_layer_set_font(&s_data.direction_layer, fonts_get_system_font(FONT_KEY_GOTHIC_14_BOLD));
  text_layer_set_text_alignment(&s_data.direction_layer, GTextAlignmentLeft);
  layer_add_child(&window->layer, &s_data.direction_layer.layer);
	
  text_layer_init(&s_data.time_layer, GRect(0, 150, 144, 20));
  text_layer_set_text_color(&s_data.time_layer, GColorWhite);
  text_layer_set_background_color(&s_data.time_layer, GColorClear);
  text_layer_set_font(&s_data.time_layer, fonts_get_system_font(FONT_KEY_GOTHIC_18_BOLD));
  text_layer_set_text_alignment(&s_data.time_layer, GTextAlignmentLeft);
  layer_add_child(&window->layer, &s_data.time_layer.layer);
  text_layer_set_text(&s_data.direction_layer, "Hello World");
  text_layer_set_text(&s_data.time_layer, "Hello time");


  Tuplet initial_values[] = {
    TupletInteger(GO_ICON_KEY, (uint8_t) 2),
	TupletCString(GO_TIME_KEY, "Navigation System"),
    TupletCString(GO_DIRECTION_KEY, "Welcome to Pebble Go")
  };
	
  app_sync_init(&s_data.sync, s_data.sync_buffer, sizeof(s_data.sync_buffer), initial_values, ARRAY_LENGTH(initial_values),
                sync_tuple_changed_callback, sync_error_callback, NULL);
	
   window_set_click_config_provider(window, (ClickConfigProvider) click_config_provider);
}

static void go_app_deinit(AppContextRef c) {
  app_sync_deinit(&s_data.sync);
  if (s_data.current_icon != 0) {
    heap_bitmap_deinit(&s_data.icon_bitmap);
  }
}

void pbl_main(void *params) {
  PebbleAppHandlers handlers = {
    .init_handler = &go_app_init,
    .deinit_handler = &go_app_deinit,
    .messaging_info = {
      .buffer_sizes = {
        .inbound = 256,
        .outbound = 256,
      }
    }
  };
  app_event_loop(params, &handlers);
}




# Stream Notification Bot

## Build status

[![Docker Build](https://github.com/dmitry-osin/twitch-notifier/actions/workflows/docker-build.yml/badge.svg)](https://github.com/dmitry-osin/twitch-notifier/actions/workflows/docker-build.yml)

A bot that monitors streaming platforms (currently supports Twitch) and sends notifications to Telegram when tracked streamers go live.

## Requirements

- Java 21 or higher
- Telegram Bot Token
- Twitch Developer Application credentials
- Docker (optional)

## Configuration

### Bot Configuration

Create config file named `config.json` in the `resources` directory with the following structure:

```json
{
  "twitchClientId": "YOUR_CLIENT_ID",
  "twitchClientSecret": "YOUR_CLIENT_SECRET",
  "telegramToken": "YOUR_BOT_TOKEN",
  "telegramChatId": "YOUR_CHAT_ID",
  "notificationDelayInSeconds": 60,
  "locale": "en",
  "botName": "YOUR_BOT_NAME"
}
```

- `twitchClientId` and `twitchClientSecret`: Obtain from [Twitch Developer Console](https://dev.twitch.tv/console)
- `telegramToken`: Get from [@BotFather](https://t.me/botfather) on Telegram
- `telegramChatId`: The chat ID where notifications will be sent
- `notificationDelayInSeconds`: Interval between checks (in seconds)
- `locale`: Language for notifications ("en" for English)
- `botName`: Your bot's display name

### Channels Configuration

Create a text file named `channels.txt` in the `resources` directory with the list of channels to monitor. 

Format:

```
channel1:twitch
channel2:twitch
channel3:twitch
```

Currently supported platforms:
- `twitch` - Twitch.tv streams
- `vk` - VK Play streams (not implemented yet)

## Running with Docker

1. Build the project:

```bash
./gradlew build
```

```bash
docker run -v /path/to/your/config:/app/resources stream-notification-bot
```
Make sure to mount a volume with your configuration files (`config.json` and `channels.txt`) to `/app/resources`.

## Features

- Real-time monitoring of Twitch streams
- Telegram notifications when streams go live
- Support for multiple channels
- Configurable check intervals
- Internationalization support
- Docker deployment support

## Planned Features

- VK Play platform support
- Discord notifications
- More detailed stream information
- Command interface through Telegram bot
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

The bot is configured using environment variables:

```bash
# Twitch Configuration
bot_twitchClientId=YOUR_CLIENT_ID
bot_twitchClientSecret=YOUR_CLIENT_SECRET
bot_twitchOAuthToken=YOUR_OAUTH_TOKEN

# Telegram Configuration
bot_telegramToken=YOUR_BOT_TOKEN
bot_telegramChatId=YOUR_CHAT_ID

# Bot Settings
bot_retryDelayInSeconds=60
bot_locale=en
bot_name=YOUR_BOT_NAME
bot_channels=channel1:twitch,channel2:twitch
bot_notificationMessage=Hey! {channel} is now live: {title}
```

### Environment Variables Description

- `bot_twitchClientId`, `bot_twitchClientSecret` and `bot_twitchOAuthToken`: Obtain
  from [Twitch Developer Console](https://dev.twitch.tv/console)

  To get these credentials using Twitch CLI:
    1. Install Twitch CLI from https://dev.twitch.tv/docs/cli/
    2. Authenticate with Twitch:
       ```bash
       twitch configure
       ```
    3. Create a new application:
       ```bash
       twitch api post client/create -b '{"name":"YourAppName","redirect_uri":"http://localhost"}'
       ```
       This will return your `client_id` and `client_secret`

    4. Get OAuth token:
       ```bash
       twitch token
       ```
       This will generate your `OAuth token`

- `bot_telegramToken`: Get from [@BotFather](https://t.me/botfather) on Telegram
- `bot_telegramChatId`: The chat ID where notifications will be sent
- `bot_retryDelayInSeconds`: Interval between checks (in seconds)
- `bot_locale`: Language for notifications ("en" for English)
- `bot_name`: Your bot's display name
- `bot_channels`: Comma-separated list of channels to monitor (format: channelname:platform)
- `bot_notificationMessage`: Template for notification messages. Available variables: {channel}, {title}

Currently supported platforms:
- `twitch` - Twitch.tv streams
- `vk` - VK Play streams (not implemented yet)

## Running with Docker

1. Build the project:

```bash
./gradlew build
```

2. Run the container with environment variables:

```bash
docker run \
  -e bot_twitchClientId=YOUR_CLIENT_ID \
  -e bot_twitchClientSecret=YOUR_CLIENT_SECRET \
  -e bot_telegramToken=YOUR_BOT_TOKEN \
  -e bot_telegramChatId=YOUR_CHAT_ID \
  -e bot_retryDelayInSeconds=60 \
  -e bot_locale=en \
  -e bot_name=YOUR_BOT_NAME \
  -e bot_channels=channel1:twitch,channel2:twitch \
  -e bot_notificationMessage="Hey! {channel} is now live: {title}" \
  stream-notification-bot
```

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

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
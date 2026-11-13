# Amora 💜 — the My-Love Platform

**One secure home for messaging, dating, social and work — built for the whole world.**

Amora is a native Android app (Kotlin + Jetpack Compose) that combines a messenger, a dating experience, a social feed and collaboration spaces in a single, beautifully designed product — powered entirely by **open-source technology**.

<p>
  <a href="https://github.com/shakil603/My-love/releases/latest"><b>⬇️ Download the latest APK</b></a>
  · Android 8.0+ · ~19 MB · no account required
</p>

---

## ✨ What's inside

### 💬 Real messaging (not a demo)
- **Live rooms over the open MQTT network** (Eclipse Paho client) — install the app on two phones anywhere on Earth and chat in real time
- **Global room** for everyone + **private rooms** joined by a shared code
- **End-to-end encryption**: every live message is sealed with **AES-256-GCM**, key derived from the room code — the broker only ever sees ciphertext
- Rich chat UI: reactions, reply/quote, voice-message bubbles with waveform, file bubbles with previews, typing indicators, read ticks, disappearing-messages mode, group chats, pinned chats, search & filters
- Sent messages **persist on device** across app restarts

### 📞 Real audio & video calls
- **WebRTC calls on an open-source Jitsi Meet server** — encrypted in transit (DTLS-SRTP), nothing stored
- Start a voice or video call from any live room; the other side gets an instant invite message and joins with one tap
- Designed call screens: incoming call, in-call video with PiP self-view, voice call with live timer and full controls

### 💘 Dating
- Swipe-style Discover with real profile cards, match percentages, interests
- Match celebration, Liked-you grid, People-nearby list
- Full user profiles: cover, bio, verified badge, photo gallery, audio/video call and message actions

### 🌍 Social & spaces
- Feed with stories, posts, likes, working comments, file-attachment posts
- Reels, story viewer, notifications, search
- Spaces: unlimited **group creation**, shared media, events, notes & tasks, calendar, saved items

### 🌐 Built for the world
- **14 languages** (English, বাংলা, العربية, Español, हिन्दी, 中文, Français, Português, Русский, 日本語, Deutsch, Türkçe, اردو, Bahasa Indonesia)
- Real Android **per-app language support** — switch inside the app *or* from Android Settings → Apps → Amora → Language (Android 13+)
- Full RTL layout support
- Runtime permission onboarding (camera, mic, notifications, media, location)

---

## 🔓 Open-source stack

| Layer | Technology |
|---|---|
| App | Kotlin · Jetpack Compose · Material 3 |
| Messaging | MQTT (Eclipse Paho) over a public open broker — swappable for self-hosted Mosquitto/EMQX or a broker on your own device |
| Message security | AES-256-GCM end-to-end encryption, per-room keys, random IV per message |
| Calls | Jitsi Meet (WebRTC, DTLS-SRTP) on a community-run open-source server — self-hostable |
| Images | Coil |
| CI/CD | GitHub Actions — every push builds and publishes an APK release |

No proprietary backend. No data silo. Every piece of infrastructure can be replaced with your own open-source deployment — or run on users' own devices.

---

## 📲 Install

1. Grab `Amora-vX.Y.Z-debug.apk` from the [latest release](https://github.com/shakil603/My-love/releases/latest)
2. Allow "install from unknown sources" when prompted
3. Open Amora → sign up → grant permissions → done

**Try real messaging:** two phones → Chats → *Live messaging* → join the same private room code → chat, then tap the video icon for a real encrypted call.

## 🛠 Build from source

```bash
git clone https://github.com/shakil603/My-love.git
cd My-love
./gradlew assembleDebug   # or let GitHub Actions build it for you
```

## 📁 Project structure

```
app/src/main/java/com/amora/app/
├── MainActivity.kt   # Navigation graph (35+ routes)
├── Theme.kt          # Aurora design system (rose → orchid → violet)
├── Mock.kt           # Demo data + on-device persistence (Store)
├── Screens.kt        # Onboarding · Feed · Discover · Spaces · Profile
├── Screens2.kt       # Signup · OTP · Match · Story · Reels · Notifications · …
├── Messenger.kt      # Chat list + conversation (reactions, voice, groups)
├── LiveChat.kt       # Real MQTT messaging + AES-256-GCM E2E encryption
├── RealCall.kt       # Real Jitsi/WebRTC call screen
├── Calls.kt          # Audio/video/incoming call UI
├── Profiles.kt       # User profiles · edit profile · followers
├── Social.kt         # Liked-you · nearby · group info · saved · events
└── Extras.kt         # Language picker · permissions · create group
```

---

Made with 💜 in Mymensingh, Bangladesh — for everyone, everywhere.

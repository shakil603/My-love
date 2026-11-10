package com.amora.app

data class Story(val name: String, val url: String, val isAdd: Boolean = false)

data class Post(
    val author: String,
    val avatar: String,
    val meta: String,
    val text: String,
    val image: String?,
    val likes: String,
    val comments: String,
    val verified: Boolean = false,
    val fileName: String? = null,
    val fileMeta: String? = null
)

data class Chat(
    val id: String,
    val name: String,
    val avatar: String,
    val last: String,
    val time: String,
    val unread: Int,
    val online: Boolean
)

data class Message(
    val mine: Boolean,
    val text: String? = null,
    val fileName: String? = null,
    val fileMeta: String? = null,
    val image: String? = null,
    val time: String
)

object Mock {
    private const val U = "https://images.unsplash.com"

    val stories = listOf(
        Story("You", "", isAdd = true),
        Story("Sofia", "$U/photo-1494790108377-be9c29b29330?w=200"),
        Story("Rafi", "$U/photo-1500648767791-00dcc994a43e?w=200"),
        Story("Yuki", "$U/photo-1534528741775-53994a69daeb?w=200"),
        Story("Omar", "$U/photo-1506794778202-cad84cf45f1d?w=200"),
        Story("Amara", "$U/photo-1524504388940-b1c1722653e1?w=200")
    )

    val posts = listOf(
        Post(
            "Sofia Martínez", "$U/photo-1494790108377-be9c29b29330?w=200",
            "Barcelona · 12 min · Public",
            "Golden hour on the coast — sometimes the best plans are no plans at all \uD83E\uDDE1",
            "$U/photo-1507525428034-b723cf961d3e?w=800",
            "2.4k", "186", verified = true
        ),
        Post(
            "Daniel Okafor", "$U/photo-1472099645785-5658abf4ff4e?w=200",
            "Product Lead · 1 h · Network",
            "Sharing our open-source launch playbook — 40 pages, free for everyone \uD83D\uDE80",
            null, "891", "92",
            fileName = "Launch-Playbook-2026.pdf", fileMeta = "40 pages · 8.2 MB · Tap to preview"
        ),
        Post(
            "Yuki Tanaka", "$U/photo-1534528741775-53994a69daeb?w=200",
            "Tokyo · 31 min · Public",
            "Night walk in Shibuya. The city never sleeps and honestly, same \uD83C\uDF03",
            "$U/photo-1540959733332-eab4deabeeaf?w=800",
            "8.1k", "432"
        )
    )

    val chats = listOf(
        Chat("1", "Amara \uD83D\uDC98", "$U/photo-1524504388940-b1c1722653e1?w=200", "typing…", "now", 2, true),
        Chat("2", "Project Phoenix \uD83D\uDCBC", "$U/photo-1522071820081-009f0129c71c?w=200", "\uD83D\uDCCA Nadia: Q3-Forecast.xlsx · 2.1 MB", "09:12", 5, false),
        Chat("3", "Rafi Ahmed", "$U/photo-1500648767791-00dcc994a43e?w=200", "\uD83C\uDF99 Voice message · 0:42", "08:47", 0, true),
        Chat("4", "Family ❤️", "$U/photo-1511895426328-dc8714191300?w=200", "\uD83D\uDCF7 Mum shared 4 photos", "Yesterday", 0, false),
        Chat("5", "Yuki Tanaka", "$U/photo-1534528741775-53994a69daeb?w=200", "\uD83C\uDFAC trailer_final_v3.mp4 · seen", "Yesterday", 0, false),
        Chat("6", "Omar Haddad", "$U/photo-1506794778202-cad84cf45f1d?w=200", "\uD83D\uDDDC assets-pack.zip · 48 files", "Mon", 0, false)
    )

    val messages = listOf(
        Message(false, text = "Hey! Did you check the gallery I sent? \uD83D\uDDBC", time = "09:02"),
        Message(false, image = "$U/photo-1506905925346-21bda4d32df4?w=500", time = "09:02"),
        Message(true, fileName = "Trip-Itinerary-Bali.pdf", fileMeta = "12 pages · 3.4 MB · tap to preview", time = "09:05"),
        Message(false, text = "This looks amazing!! Counting the days \uD83E\uDD29", time = "09:06"),
        Message(true, text = "Can't wait! Also sending the budget sheet for the trip \uD83E\uDDFE", time = "09:07"),
        Message(true, fileName = "Bali-Budget.xlsx", fileMeta = "3 sheets · 214 KB · live preview ✓", time = "09:07")
    )

    val profileGrid = listOf(
        "$U/photo-1506905925346-21bda4d32df4?w=300",
        "$U/photo-1470770841072-f978cf4d019e?w=300",
        "$U/photo-1501785888041-af3ef285b470?w=300",
        "$U/photo-1519904981063-b0cf448d479e?w=300",
        "$U/photo-1507525428034-b723cf961d3e?w=300",
        "$U/photo-1540959733332-eab4deabeeaf?w=300"
    )

    val discover = Triple(
        "$U/photo-1524504388940-b1c1722653e1?w=700",
        "Amara, 26",
        "4 km away · UX Designer · 94% match"
    )
}

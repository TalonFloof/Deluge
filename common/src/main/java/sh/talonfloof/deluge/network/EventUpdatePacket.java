package sh.talonfloof.deluge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import sh.talonfloof.deluge.Deluge;
import sh.talonfloof.deluge.DelugeEventType;

public record EventUpdatePacket(DelugeEventType eventType) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EventUpdatePacket> TYPE = new CustomPacketPayload.Type<>(Deluge.path("event_update"));

    public static final StreamCodec<ByteBuf, EventUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            (self) -> self.eventType().ordinal(),
            (eventType) -> new EventUpdatePacket(DelugeEventType.values()[eventType])
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void send(ServerPlayer player, DelugeEventType eventType) {
        if(Deluge.NETWORK.canSend(TYPE.id(), player)) {
            Deluge.NETWORK.send(new EventUpdatePacket(eventType), player);
        }
    }
}

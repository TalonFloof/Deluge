package sh.talonfloof.deluge.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import sh.talonfloof.deluge.Deluge;
import sh.talonfloof.deluge.DelugeEventType;

public record EventUpdatePacket(DelugeEventType eventType, float rainLevel) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<EventUpdatePacket> TYPE = new CustomPacketPayload.Type<>(Deluge.path("event_update"));

    public static final StreamCodec<ByteBuf, EventUpdatePacket> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            (self) -> self.eventType().ordinal(),
            ByteBufCodecs.FLOAT,
            EventUpdatePacket::rainLevel,
            (eventType, rainLevel) -> new EventUpdatePacket(DelugeEventType.values()[eventType],rainLevel)
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void send(ServerPlayer player, DelugeEventType eventType, float rainLevel) {
        if(Deluge.NETWORK.canSend(TYPE.id(), player)) {
            Deluge.NETWORK.send(new EventUpdatePacket(eventType, rainLevel), player);
        }
    }
}

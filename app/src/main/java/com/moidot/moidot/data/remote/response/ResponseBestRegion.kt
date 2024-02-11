package com.moidot.moidot.data.remote.response

import android.os.Parcel
import android.os.Parcelable

data class ResponseBestRegion(
    val data: List<Data>
) : Parcelable, BaseResponse() {
    constructor(parcel: Parcel) : this(parcel.createTypedArrayList(Data.CREATOR)!!)

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeTypedList(data)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ResponseBestRegion> {
        override fun createFromParcel(parcel: Parcel): ResponseBestRegion {
            return ResponseBestRegion(parcel)
        }

        override fun newArray(size: Int): Array<ResponseBestRegion?> {
            return arrayOfNulls(size)
        }
    }

    data class Data(
        val latitude: Double,
        val longitude: Double,
        val moveUserInfo: List<MoveUserInfo>,
        val name: String
    ) : Parcelable {
        constructor(parcel: Parcel) : this(
            parcel.readDouble(),
            parcel.readDouble(),
            parcel.createTypedArrayList(MoveUserInfo.CREATOR)!!,
            parcel.readString()!!
        )

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            parcel.writeDouble(latitude)
            parcel.writeDouble(longitude)
            parcel.writeTypedList(moveUserInfo)
            parcel.writeString(name)
        }

        override fun describeContents(): Int {
            return 0
        }

        companion object CREATOR : Parcelable.Creator<Data> {
            override fun createFromParcel(parcel: Parcel): Data {
                return Data(parcel)
            }

            override fun newArray(size: Int): Array<Data?> {
                return arrayOfNulls(size)
            }
        }

        data class MoveUserInfo(
            val isAdmin: Boolean,
            val path: List<Path>,
            val payment: Int,
            val totalDistance: Int,
            val totalTime: Int,
            val transitCount: Int,
            val transportationType: String,
            val userId: Int,
            val userName: String
        ) : Parcelable {
            constructor(parcel: Parcel) : this(
                parcel.readByte() != 0.toByte(),
                parcel.createTypedArrayList(Path.CREATOR)!!,
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readInt(),
                parcel.readString()!!,
                parcel.readInt(),
                parcel.readString()!!
            )

            override fun writeToParcel(parcel: Parcel, flags: Int) {
                parcel.writeByte(if (isAdmin) 1 else 0)
                parcel.writeTypedList(path)
                parcel.writeInt(payment)
                parcel.writeInt(totalDistance)
                parcel.writeInt(totalTime)
                parcel.writeInt(transitCount)
                parcel.writeString(transportationType)
                parcel.writeInt(userId)
                parcel.writeString(userName)
            }

            override fun describeContents(): Int {
                return 0
            }

            companion object CREATOR : Parcelable.Creator<MoveUserInfo> {
                override fun createFromParcel(parcel: Parcel): MoveUserInfo {
                    return MoveUserInfo(parcel)
                }

                override fun newArray(size: Int): Array<MoveUserInfo?> {
                    return arrayOfNulls(size)
                }
            }

            data class Path(
                val x: Double,
                val y: Double
            ) : Parcelable {
                constructor(parcel: Parcel) : this(
                    parcel.readDouble(),
                    parcel.readDouble()
                )

                override fun writeToParcel(parcel: Parcel, flags: Int) {
                    parcel.writeDouble(x)
                    parcel.writeDouble(y)
                }

                override fun describeContents(): Int {
                    return 0
                }

                companion object CREATOR : Parcelable.Creator<Path> {
                    override fun createFromParcel(parcel: Parcel): Path {
                        return Path(parcel)
                    }

                    override fun newArray(size: Int): Array<Path?> {
                        return arrayOfNulls(size)
                    }
                }
            }
        }
    }
}

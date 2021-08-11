package won.young.challengecamera.dbutils.challengelist

data class ChallengeListDTO(
    var challengeName: String? = null,
    var startDate: Long? = null,
    var endDate: Long? = null,
    var kind: Int? = null,
    var isAlarmOn: Boolean? = null,
    var isHideOn: Boolean? = null,
    var alarmAmPm: Boolean? = null,
    var alarmTime: Int? = null,
    var alarmMinute: Int? = null
)
const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return `${[year, month, day].map(formatNumber).join('/')} ${[hour, minute, second].map(formatNumber).join(':')}`
}

const formatDate = date =>{
  const year = date.getFullYear()
  let month = date.getMonth() + 1
  let day = date.getDate()
  if(month<10)
    month = "0"+month
  if(day<10)
    day = "0"+day
  return year + '-' + month + '-' + day
}

const formatMonthDay = date =>{
  const year = date.getFullYear()
  let month = date.getMonth() + 1
  let day = date.getDate()
  if(month<10)
    month = "0"+month
  if(day<10)
    day = "0"+day
  return month + '-' + day
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : `0${n}`
}

const bindAndSet = (e,app) => {
  let name = e.currentTarget.dataset.inpval
  app.data[name] = e.detail.value
}

module.exports = {
  formatTime,
  bindAndSet,
  formatDate,
  formatMonthDay
}

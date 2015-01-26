import sys
class BinaryCode:

	def gameOver(self, list):
		return True if list[0] == "NONE" else False

	def listToStr(self, list):
		if list[0]=='NONE':
			return list[0]
		return ''.join(str(i) for i in list)

	def decode(self, args):
		encodedString = [int(i) for i in args]
		decodedMessage0 = [0]
		decodedMessage1 = [1]

		decodedMessage0.append(encodedString[0] - decodedMessage0[0])
		decodedMessage1.append(encodedString[0] - decodedMessage1[0])

		for i in range(2,len(encodedString)):
			if not self.gameOver(decodedMessage0):
				decodedMessage0.append(encodedString[i-1] - decodedMessage0[i-1] - decodedMessage0[i-2])
				if decodedMessage0[i] !=0 and decodedMessage0[i] != 1:
					decodedMessage0 = ["NONE"]
			if not self.gameOver(decodedMessage1):
				decodedMessage1.append(encodedString[i-1] - decodedMessage1[i-1] - decodedMessage1[i-2])
				if decodedMessage1[i] !=0 and decodedMessage1[i] != 1:
					print 'BROKEN'
					decodedMessage1 = ["NONE"]
		print '{' + self.listToStr(decodedMessage0) + ', ' + self.listToStr(decodedMessage1) + '}'

class Exec:

	if __name__ == '__main__':
		obj = BinaryCode()
		obj.decode(sys.argv[1])
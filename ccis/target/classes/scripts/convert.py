from docx2pdf import convert
import sys

def convert_to_pdf(input_path, output_path):
    try:
        convert(input_path, output_path)
        print("SUCCESS")  # Signal success to Java
    except Exception as e:
        print(f"ERROR: {str(e)}")  # Signal error to Java
        sys.exit(1)

if __name__ == "__main__":
    if len(sys.argv) != 3:
        print("ERROR: Requires input and output path arguments")
        sys.exit(1)
    
    convert_to_pdf(sys.argv[1], sys.argv[2])